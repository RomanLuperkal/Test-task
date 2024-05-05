package com.warehouse.myshop.order.service;

import com.warehouse.myshop.curt.model.Cart;
import com.warehouse.myshop.curt.model.CartKey;
import com.warehouse.myshop.curt.repository.CartRepository;
import com.warehouse.myshop.customer.model.Customer;
import com.warehouse.myshop.customer.repository.CustomerRepository;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.handler.exceptions.OrderException;
import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.dto.ResponseOrderDto;
import com.warehouse.myshop.order.mapper.OrderMapper;
import com.warehouse.myshop.order.model.Ordering;
import com.warehouse.myshop.order.repository.OrderRepository;
import com.warehouse.myshop.product.dto.ShortProductDto;
import com.warehouse.myshop.product.model.Product;
import com.warehouse.myshop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
        List<UUID> productIds = orderProducts.stream().map(ShortProductDto::getId).collect(Collectors.toList());
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id=" + customerId + " не существует"));
        /*if (!productRepository.existsAllByUuidIn(productIds)) {
            throw new OrderException("В корзине присутствуют несуществующие товары товары");
        }*/
        List<Product> products = (ArrayList) productRepository.findAllById(productIds);
        Ordering ordering = mapper.mapToOrder(orderDto);
        ordering.setCustomer(customer);
        ordering.setDeliveryAddress(orderDto.getDeliveryAddress());
        Ordering savedOrdering = orderRepository.save(ordering);
        //todo проверить is_avaiable
        for (ShortProductDto shortProduct : orderProducts) {
            Product product = products.stream().filter(p -> p.getUuid().equals(shortProduct.getId())).findFirst()
                    .orElseThrow(() -> new OrderException("В корзине присутствуют несуществующие товары товары"));
            int totalQuantity = product.getQuantity() - shortProduct.getQuantity();
            if (totalQuantity < 0) {
                throw new OrderException("Товара с " + shortProduct.getId() + " нет в достаточной количестве");
            } else {
                product.setQuantity(totalQuantity);
            }
            /*if (!productRepository.isSufficientProductInStock(shortProduct.getId(), shortProduct.getQuantity())) {
                throw new OrderException("Товара с " + shortProduct.getId() + " нет в достаточной количестве");
            }*/
            //productRepository.updateProductQuantityByUuid(shortProduct.getId(), shortProduct.getQuantity());
           // BigDecimal price = productRepository.getPriceByUuid(shortProduct.getId());
            /*curtRepository.insertCart(savedOrdering.getId(),product.getId(), product.getQuantity(),
                    price.multiply(BigDecimal.valueOf(product.getQuantity())));*/
            Cart cart = new Cart();
            //CartKey cartKey = new CartKey(savedOrdering, product);
            //cart.setId(cartKey);
            cart.setOrder(savedOrdering);
            cart.setProduct(product);
            cart.setQuantity(shortProduct.getQuantity());
            cart.setPrice(product.getPrice());
            cartRepository.save(cart);
        }

       return null;
    }
}
