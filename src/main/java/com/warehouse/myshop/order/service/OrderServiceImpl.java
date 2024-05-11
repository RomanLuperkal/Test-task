package com.warehouse.myshop.order.service;

import com.warehouse.myshop.customer.model.Customer;
import com.warehouse.myshop.customer.repository.CustomerRepository;
import com.warehouse.myshop.handler.exceptions.AccessException;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.handler.exceptions.OrderException;
import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.dto.ResponseFullOrderDto;
import com.warehouse.myshop.order.dto.ResponseOrderDto;
import com.warehouse.myshop.order.dto.StatusDto;
import com.warehouse.myshop.order.enums.Status;
import com.warehouse.myshop.order.mapper.OrderMapper;
import com.warehouse.myshop.order.model.Order;
import com.warehouse.myshop.order.repository.OrderRepository;
import com.warehouse.myshop.orderedproduct.model.OrderedProduct;
import com.warehouse.myshop.orderedproduct.repository.CartRepository;
import com.warehouse.myshop.product.dto.ProductDto;
import com.warehouse.myshop.product.dto.ShortProductDto;
import com.warehouse.myshop.product.model.Product;
import com.warehouse.myshop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final OrderMapper mapper;

    @Override
    @Transactional
    public ResponseOrderDto createOrder(CreateOrderDto orderDto, Long customerId) {
        List<ShortProductDto> orderProducts = orderDto.getProducts();
        if (orderProducts.stream().map(ShortProductDto::getId).distinct().count() != orderProducts.size()) {
            throw new OrderException("В заказе присутсвуют дублирующиеся товары");
        }
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id=" + customerId + " не существует"));
        Set<UUID> productIds = orderProducts.stream().map(ShortProductDto::getId).collect(Collectors.toSet());
        List<Product> products = productRepository.findAllByUuidIn(productIds);
        Order order = mapper.mapToOrder(orderDto);
        order.setCustomer(customer);
        order.setDeliveryAddress(orderDto.getDeliveryAddress());
        Order savedOrder = orderRepository.save(order);
        List<OrderedProduct> newOrderedProducts = createOrderedProducts(orderProducts, products, savedOrder);
        cartRepository.saveAll(newOrderedProducts);
        return mapper.mapToResponseOrderDto(order);
    }

    @Override
    @Transactional
    public ResponseOrderDto updateOrder(List<ShortProductDto> orderProducts, Long customerId, UUID orderId) {
        Set<UUID> productIds = orderProducts.stream().map(ShortProductDto::getId).collect(Collectors.toSet());
        if (!productRepository.isExistsProducts(productIds, (long) productIds.size())) {
            throw new OrderException("В заказе присутсвуют несуществующие товары");
        }
        Order order = orderRepository.findFullOrderByOrderId(orderId)
                .orElseThrow(() -> new OrderException("Заказа с id=" + orderId + " не существует"));
        validateCustomer(order, customerId);
        if (!order.getStatus().equals(Status.CREATED)) {
            throw new OrderException("Изменить можно только заказ со статусом CREATED");
        }

        Set<OrderedProduct> orderedProducts = order.getOrderedProducts();

        List<Product> products = productRepository.findAllByUuidIn(productIds);
        List<OrderedProduct> newOrderedProducts = updateCarts(orderProducts, orderedProducts, products, order);
        cartRepository.saveAll(newOrderedProducts);
        return mapper.mapToResponseOrderDto(order);
    }

    @Override
    public ResponseFullOrderDto getOrder(UUID orderId, Long customerId) {
        Order order = orderRepository.findOrderWithCustomerByOrderId(orderId)
                .orElseThrow(() -> new OrderException("Заказа с id=" + orderId + " не существует"));
        validateCustomer(order, customerId);
        List<ProductDto> products = cartRepository.findOrderProductsByOrderId(orderId);
        BigDecimal totalPrice = products.stream().map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ResponseFullOrderDto.builder()
                .orderId(orderId)
                .products(products)
                .totalPrice(totalPrice)
                .build();
    }

    @Override
    @Transactional
    public void deleteOrder(UUID orderId, Long customerId) {
        Order order = orderRepository.findFullOrderByOrderId(orderId)
                .orElseThrow(() -> new OrderException("Заказа с id=" + orderId + " не существует"));
        validateCustomer(order, customerId);
        if (order.getStatus() != Status.CREATED) {
            throw new OrderException("Удалить можно только заказ находящийся в статусе CREATED");
        }
        order.setStatus(Status.CANCELLED);
        Set<OrderedProduct> orderedProducts = order.getOrderedProducts();
        orderedProducts.forEach(c -> {
            Product product = c.getProduct();
            product.setQuantity(product.getQuantity() + c.getQuantity());
        });
    }

    @Override
    @Transactional
    public ResponseOrderDto changeStatusOrder(UUID orderId, StatusDto status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Заказа с id=" + orderId + " не существует"));
        order.setStatus(status.getStatus());
        return mapper.mapToResponseOrderDto(order);
    }

    private int calculateQuantity(Integer actualQuantity, Integer orderingQuantity) {
        int totalQuantity = actualQuantity - orderingQuantity;
        if (totalQuantity < 0) {
            throw new OrderException("Товара нет в достаточном количестве");
        }
        return totalQuantity;
    }

    private List<OrderedProduct> createOrderedProducts(List<ShortProductDto> orderProducts, List<Product> products, Order savedOrder) {
        List<OrderedProduct> newOrderedProducts = new ArrayList<>();
        Map<UUID, Product> mapProducts = products.stream().collect(Collectors.toMap(Product::getUuid, p -> p));
        for (ShortProductDto shortProduct : orderProducts) {
            Product product = mapProducts.get(shortProduct.getId());
            if (product == null) {
                throw new OrderException("В корзине присутствуют несуществующие товары товары");
            }
            if (!product.getIsAvailable()) {
                throw new OrderException("Товар с id=" + product.getUuid() + " недоспупен к заказу");
            }
            int totalQuantity = calculateQuantity(product.getQuantity(), shortProduct.getQuantity());
            product.setQuantity(totalQuantity);


            OrderedProduct orderedProduct = new OrderedProduct();

            orderedProduct.setOrder(savedOrder);
            orderedProduct.setProduct(product);
            orderedProduct.setQuantity(shortProduct.getQuantity());
            orderedProduct.setPrice(product.getPrice());
            newOrderedProducts.add(orderedProduct);
        }
        return newOrderedProducts;
    }

    private List<OrderedProduct> updateCarts(List<ShortProductDto> orderProducts, Set<OrderedProduct> orderedProducts, List<Product> products, Order order) {
        List<OrderedProduct> newOrderedProducts = new ArrayList<>();

        Map<UUID, OrderedProduct> mapOrderedProducts = orderedProducts.stream()
                .collect(Collectors.toMap(op -> op.getProduct().getUuid(), op -> op));
        for (ShortProductDto shortProduct : orderProducts) {
            OrderedProduct orderedProduct = mapOrderedProducts.get(shortProduct.getId());
            if (orderedProduct != null) {
                int orderingQuantity = orderedProduct.getQuantity() + shortProduct.getQuantity();
                int totalQuantityProduct = calculateQuantity(orderedProduct.getProduct().getQuantity(), shortProduct.getQuantity());
                orderedProduct.setQuantity(orderingQuantity);
                orderedProduct.getProduct().setQuantity(totalQuantityProduct);
                orderedProduct.setPrice(orderedProduct.getProduct().getPrice());
            } else {
                orderedProduct = new OrderedProduct();
                Product product = products.stream().filter(p -> p.getUuid().equals(shortProduct.getId()))
                        .findFirst().orElseThrow(() -> new OrderException("В корзине присутствуют несуществующие товары товары"));
                int totalQuantity = calculateQuantity(product.getQuantity(), shortProduct.getQuantity());
                product.setQuantity(totalQuantity);
                orderedProduct.setOrder(order);
                orderedProduct.setProduct(product);
                orderedProduct.setQuantity(shortProduct.getQuantity());
                orderedProduct.setPrice(product.getPrice());
                newOrderedProducts.add(orderedProduct);
            }
        }
        return newOrderedProducts;
    }

    private void validateCustomer(Order order, Long customerId) {
        if (!order.getCustomer().getId().equals(customerId)) {
            throw new AccessException("Пользователь не является владельцем данного заказа");
        }
    }
}
