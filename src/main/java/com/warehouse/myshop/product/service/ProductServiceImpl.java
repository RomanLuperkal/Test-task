package com.warehouse.myshop.product.service;

import com.warehouse.myshop.account.client.AccountServiceClient;
import com.warehouse.myshop.category.model.Category;
import com.warehouse.myshop.category.repository.CategoryRepository;
import com.warehouse.myshop.crm.CrmProvider;
import com.warehouse.myshop.currency.ExchangeRateProvider;
import com.warehouse.myshop.currency.enums.Currency;
import com.warehouse.myshop.currency.session.CurrencyProvider;
import com.warehouse.myshop.customer.dto.CustomerInfo;
import com.warehouse.myshop.currency.session.CurrencyProvider;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.order.dto.OrderInfo;
import com.warehouse.myshop.order.model.Order;
import com.warehouse.myshop.orderedproduct.model.OrderedProduct;
import com.warehouse.myshop.orderedproduct.repository.OrderedProductRepository;
import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.dto.condition.FilterConditionDto;
import com.warehouse.myshop.product.mapper.ProductMapper;
import com.warehouse.myshop.product.model.Product;
import com.warehouse.myshop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CurrencyProvider currencyProvider;
    private final ProductMapper mapper;
    private final ExchangeRateProvider rateProvider;
    private final OrderedProductRepository orderedProductRepository;
    private final AccountServiceClient accountClient;
    private final CrmProvider crmProvider;

    @Override
    @Transactional
    public ResponseProductDto createProduct(NewProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Категории с id=" + productDto.getCategoryId() + " не найдено"));
        Product product = mapper.mapToProduct(productDto);
        product.setCategory(category);
        return mapper.mapToResponseProductDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ResponseProductDto updateProduct(UUID uuid, UpdateProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Категории с id=" + productDto.getCategoryId() + " не найдено"));
        Product product = productRepository.findById(uuid).orElseThrow(
                () -> new NotFoundException("Товара с UUID=" + uuid + " не существует"));
        product.setCategory(category);
        return mapper.mapToResponseProductDto(mapper.mapToProduct(product, productDto));
    }

    @Override
    @Transactional
    public void deleteProduct(UUID uuid) {
        if (!productRepository.existsById(uuid))
            throw new NotFoundException("Товара с UUID=" + uuid + " не существует");
        productRepository.deleteById(uuid);
    }

    @Override
    public ResponseProductDto getProduct(UUID uuid) {
        Product product = productRepository.findById(uuid).orElseThrow(
                () -> new NotFoundException("Товара с UUID=" + uuid + " не существует"));
        ResponseProductDto responseProductDto = mapper.mapToResponseProductDto(product);
        Currency currency = currencyProvider.getCurrency();
        responseProductDto.setCurrency(currency);
        if (!currency.equals(Currency.RUB)) {
            convertPrice(responseProductDto, rateProvider.getExchangeRate(currency));
        }
        return responseProductDto;
    }

    @Override
    public ListProductDto getProducts(Pageable pageable) {
        List<ResponseProductDto> responseProducts = mapper.mapToListResponseProductDto(productRepository.findAll(pageable));
        Currency currency = currencyProvider.getCurrency();
        setCurrencyInProducts(responseProducts, currency);
        if (!currency.equals(Currency.RUB)) {
            responseProducts.forEach(p -> convertPrice(p, rateProvider.getExchangeRate(currency)));
        }
        return ListProductDto.builder()
                .products(responseProducts)
                .build();
    }

    @Override
    public ListProductDto searchProducts(List<FilterConditionDto<?>> conditions, Pageable pageable) {
        List<Specification<Product>> specifications = mapper.mapToListSpecification(conditions);
        Specification<Product> resultSpecification = specifications.stream().reduce(Specification::and)
                .orElse(Specification.where(null));
        List<ResponseProductDto> products = mapper
                .mapToListResponseProductDto(productRepository.findAll(resultSpecification, pageable));
        Currency currency = currencyProvider.getCurrency();
        setCurrencyInProducts(products, currency);
        if (!currency.equals(Currency.RUB)) {
            products.forEach(p -> convertPrice(p, rateProvider.getExchangeRate(currency)));
        }
        return ListProductDto
                .builder()
                .products(products)
                .build();
    }

    @Override
    public Map<UUID, List<OrderInfo>> getProductsInfo() {
        List<OrderedProduct> findOrderedProducts = orderedProductRepository.findAll();
        Set<String> customerLogins = findOrderedProducts.stream().map(op -> op.getOrder().getCustomer().getLogin()).collect(Collectors.toSet());
        CompletableFuture<Map<String, String>> featureAccountNumbers = accountClient.getAccountNumbers(customerLogins);
        CompletableFuture<Map<String, String>> featureInn = crmProvider.getInns(customerLogins);
        return findOrderedProducts.stream().collect(Collectors.groupingBy(
                orderedProduct -> orderedProduct.getProduct().getUuid(),
                Collectors.mapping(
                        orderedProduct -> {
                            Order order = orderedProduct.getOrder();
                            String login = order.getCustomer().getLogin();

                            CustomerInfo customerInfo = new CustomerInfo(
                                    order.getCustomer().getId(),
                                    featureAccountNumbers.join().get(login),
                                    order.getCustomer().getEmail(),
                                    featureInn.join().get(login)
                            );

                            return new OrderInfo(
                                    order.getId(),
                                    customerInfo,
                                    order.getStatus(),
                                    order.getDeliveryAddress(),
                                    orderedProduct.getQuantity()
                            );
                        },
                        Collectors.toList()
                )
        ));
    }

    private static void convertPrice(ResponseProductDto responseProduct, BigDecimal currency) {
        BigDecimal rubPrice = responseProduct.getPrice();
        responseProduct.setPrice(rubPrice.divide(currency, 2, RoundingMode.HALF_UP));
    }

    private void setCurrencyInProducts(List<ResponseProductDto> products, Currency currency) {
        products.forEach(p -> p.setCurrency(currency));
    }
}
