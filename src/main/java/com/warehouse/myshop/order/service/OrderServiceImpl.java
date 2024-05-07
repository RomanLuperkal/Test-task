package com.warehouse.myshop.order.service;

import com.warehouse.myshop.cart.model.Cart;
import com.warehouse.myshop.cart.repository.CartRepository;
import com.warehouse.myshop.customer.model.Customer;
import com.warehouse.myshop.customer.repository.CustomerRepository;
import com.warehouse.myshop.handler.exceptions.AccessException;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.handler.exceptions.OrderException;
import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.dto.ResponseFullOrderDto;
import com.warehouse.myshop.order.dto.ResponseOrderDto;
import com.warehouse.myshop.order.enums.Status;
import com.warehouse.myshop.order.mapper.OrderMapper;
import com.warehouse.myshop.order.model.Order;
import com.warehouse.myshop.order.repository.OrderRepository;
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
import java.util.Optional;
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
        List<UUID> productIds = orderProducts.stream().map(ShortProductDto::getId).collect(Collectors.toList());
        List<Product> products = (ArrayList<Product>) productRepository.findAllById(productIds);
        Order order = mapper.mapToOrder(orderDto);
        order.setCustomer(customer);
        order.setDeliveryAddress(orderDto.getDeliveryAddress());
        Order savedOrder = orderRepository.save(order);
        List<Cart> newCarts = createCarts(orderProducts, products, savedOrder);
        cartRepository.saveAll(newCarts);
       return mapper.mapToResponseOrderDto(order);
    }

    @Override
    @Transactional
    public ResponseOrderDto updateOrder(List<ShortProductDto> orderProducts, Long customerId, UUID orderId) {
        List<UUID> productIds = orderProducts.stream().map(ShortProductDto::getId).collect(Collectors.toList());
        if (!productRepository.isExistsProducts(productIds, (long) productIds.size())) {
            throw new OrderException("В заказе присутсвуют несуществующие товары");
        }
        Order order = orderRepository.findFullOrderByOrderId(orderId)
                .orElseThrow(() -> new OrderException("Заказа с id=" + orderId + " не существует"));
        validateCustomer(order, customerId);
        if (!order.getStatus().equals(Status.CREATED)) {
            throw new OrderException("Изменить можно только заказ со статусом CREATED");
        }

        Set<Cart> carts = order.getCarts();

        List<Product> products = (ArrayList<Product>) productRepository.findAllById(productIds);
        List<Cart> newCarts = updateCarts(orderProducts, carts, products, order);
        cartRepository.saveAll(newCarts);
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

    private int calculateQuantity(Integer actualQuantity, Integer orderingQuantity) {
        int totalQuantity = actualQuantity - orderingQuantity;
        if (totalQuantity < 0) {
            throw new OrderException("Товара нет в достаточном количестве");
        }
        return totalQuantity;
    }

    public List<Cart> createCarts(List<ShortProductDto> orderProducts, List<Product> products, Order savedOrder) {
        List<Cart> newCarts = new ArrayList<>();
        for (ShortProductDto shortProduct : orderProducts) {
            Product product = products.stream().filter(p -> p.getUuid().equals(shortProduct.getId())).findFirst()
                    .orElseThrow(() -> new OrderException("В корзине присутствуют несуществующие товары товары"));
            if (!product.getIsAvailable()) {
                throw new OrderException("Товар с id=" + product.getUuid() + " недоспупен к заказу");
            }
            int totalQuantity = calculateQuantity(product.getQuantity(), shortProduct.getQuantity());
            product.setQuantity(totalQuantity);


            Cart cart = new Cart();

            cart.setOrder(savedOrder);
            cart.setProduct(product);
            cart.setQuantity(shortProduct.getQuantity());
            cart.setPrice(product.getPrice());
            newCarts.add(cart);
        }
        return newCarts;
    }

    public List<Cart> updateCarts(List<ShortProductDto> orderProducts, Set<Cart> carts , List<Product> products , Order order) {
        List<Cart> newCarts = new ArrayList<>();
        for (ShortProductDto shortProduct : orderProducts) {
            Optional<Cart> optionalCart = carts.stream().filter(c -> c.getProduct().getUuid().equals(shortProduct.getId())).findFirst();
            if (optionalCart.isPresent()) {
                Cart cart = optionalCart.get();
                int orderingQuantity = cart.getQuantity() + shortProduct.getQuantity();
                int totalQuantityProduct = calculateQuantity(cart.getProduct().getQuantity(), shortProduct.getQuantity());
                cart.setQuantity(orderingQuantity);
                cart.getProduct().setQuantity(totalQuantityProduct);
                cart.setPrice(cart.getProduct().getPrice());
            } else {
                Cart cart = new Cart();
                Product product = products.stream().filter(p -> p.getUuid().equals(shortProduct.getId()))
                        .findFirst().orElseThrow(() -> new OrderException("В корзине присутствуют несуществующие товары товары"));
                int totalQuantity = calculateQuantity(product.getQuantity(), shortProduct.getQuantity());
                product.setQuantity(totalQuantity);
                cart.setOrder(order);
                cart.setProduct(product);
                cart.setQuantity(shortProduct.getQuantity());
                cart.setPrice(product.getPrice());
                newCarts.add(cart);
            }
        }
        return newCarts;
    }

    private void validateCustomer(Order order, Long customerId) {
        if(!order.getCustomer().getId().equals(customerId)) {
            throw new AccessException("Пользователь не является владельцем данного заказа");
        }
    }
}
