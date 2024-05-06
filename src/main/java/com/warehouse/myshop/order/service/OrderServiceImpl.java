package com.warehouse.myshop.order.service;

import com.warehouse.myshop.cart.model.Cart;
import com.warehouse.myshop.cart.repository.CartRepository;
import com.warehouse.myshop.customer.model.Customer;
import com.warehouse.myshop.customer.repository.CustomerRepository;
import com.warehouse.myshop.handler.exceptions.AccessException;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.handler.exceptions.OrderException;
import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.dto.ResponseOrderDto;
import com.warehouse.myshop.order.enums.Status;
import com.warehouse.myshop.order.mapper.OrderMapper;
import com.warehouse.myshop.order.model.Order;
import com.warehouse.myshop.order.repository.OrderRepository;
import com.warehouse.myshop.product.dto.ShortProductDto;
import com.warehouse.myshop.product.model.Product;
import com.warehouse.myshop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        for (ShortProductDto shortProduct : orderProducts) {
            Product product = products.stream().filter(p -> p.getUuid().equals(shortProduct.getId())).findFirst()
                    .orElseThrow(() -> new OrderException("В корзине присутствуют несуществующие товары товары"));
            if (!product.getIsAvailable()) {
                throw new OrderException("Товар с id=" + product.getUuid() + " недоспупен к заказу");
            }
            //int totalQuantity = product.getQuantity() - shortProduct.getQuantity();
            int totalQuantity = calculateQuantity(product.getQuantity(), shortProduct.getQuantity());
            product.setQuantity(totalQuantity);


            Cart cart = new Cart();

            cart.setOrder(savedOrder);
            cart.setProduct(product);
            cart.setQuantity(shortProduct.getQuantity());
            cart.setPrice(product.getPrice());
            cartRepository.save(cart);
        }
       return mapper.mapToResponseOrderDto(order);
    }

    @Override
    @Transactional
    public ResponseOrderDto updateOrder(List<ShortProductDto> updateOrder, Long customerId, UUID orderId) {
        List<UUID> productIds = updateOrder.stream().map(ShortProductDto::getId).collect(Collectors.toList());
        if (!productRepository.isExistsProducts(productIds, (long) productIds.size())) {
            throw new OrderException("В заказе присутсвуют несуществующие товары");
        }
        Order order = orderRepository.findOrderByOrderId(orderId)
                .orElseThrow(() -> new OrderException("Заказа с id=" + orderId + " не существует"));
        if(!order.getCustomer().getId().equals(customerId)) {
            throw new AccessException("Пользователь не является владельцем данного заказа");
        }
        if (!order.getStatus().equals(Status.CREATED)) {
            throw new OrderException("Изменить можно только заказ со статусом CREATED");
        }

        Set<Cart> carts = order.getCarts();
        List<Cart> newCarts = new ArrayList<>();
        List<Product> products = (ArrayList<Product>) productRepository.findAllById(productIds);
        for (ShortProductDto shortProduct : updateOrder) {
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
            cartRepository.saveAll(newCarts);
        }
        return null;
    }

    private int calculateQuantity(Integer actualQuantity, Integer orderingQuantity) {
        int totalQuantity = actualQuantity - orderingQuantity;
        if (totalQuantity < 0) {
            throw new OrderException("Товара нет в достаточном количестве");
        }
        return totalQuantity;
    }
}
