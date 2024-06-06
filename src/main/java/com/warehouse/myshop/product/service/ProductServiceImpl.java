package com.warehouse.myshop.product.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.warehouse.myshop.category.model.Category;
import com.warehouse.myshop.category.repository.CategoryRepository;
import com.warehouse.myshop.configuration.S3Properties;
import com.warehouse.myshop.currency.ExchangeRateProvider;
import com.warehouse.myshop.currency.session.CurrencyProvider;
import com.warehouse.myshop.currency.enums.Currency;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.handler.exceptions.ProductException;
import com.warehouse.myshop.product.dto.condition.FilterConditionDto;
import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.mapper.ProductMapper;
import com.warehouse.myshop.product.model.Product;
import com.warehouse.myshop.product.repository.ProductRepository;
import com.warehouse.myshop.productimage.model.Image;
import com.warehouse.myshop.productimage.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final CurrencyProvider currencyProvider;
    private final ProductMapper mapper;
    private final ExchangeRateProvider rateProvider;
    private final AmazonS3 s3Client;
    private final S3Properties s3Properties;

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
    @Transactional
    public void uploadImage(UUID productId, MultipartFile file)  {
            Product product = productRepository.getProductWithImages(productId)
                    .orElseThrow(() -> new NotFoundException("Товара с UUID=" + productId + " не существует"));
            final String originalName = file.getOriginalFilename();
            String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
            String extension = originalName.substring(originalName.lastIndexOf('.'));
            long duplicateNames = product.getImages().stream()
                    .filter(i -> i.getOriginalName().equals(originalName))
                    .count();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            String newFileName;
            if (duplicateNames > 0) {
                newFileName = baseName + "(" + duplicateNames + ")" + extension;
            } else {
                newFileName = originalName;
            }

            Image image = new Image();
            image.setProduct(product);
            image.setOriginalName(originalName);
            image.setNewName(newFileName);
            Image savedImage = imageRepository.save(image);

            String bucketName = s3Properties.getBucket();
            try {
                s3Client.putObject(bucketName, savedImage.getImageUuid().toString(), file.getInputStream(), metadata);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }

    @Override
    public InputStreamResource downloadImages(UUID productId) {
            Product product = productRepository.getProductWithImages(productId)
                    .orElseThrow(() -> new NotFoundException("Товара с UUID=" + productId + " не существует"));
            Set<Image> images = product.getImages();
            if (images.isEmpty())
                throw new ProductException("У данного товара отсутсвуют картинки");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            try (ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {
                String bucketName = s3Properties.getBucket();

                for (Image image : images) {
                    String fileKey = image.getImageUuid().toString();
                    S3Object s3Object = s3Client.getObject(bucketName, fileKey);
                    S3ObjectInputStream inputStream = s3Object.getObjectContent();

                    zipOut.putNextEntry(new ZipEntry(image.getNewName()));
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        zipOut.write(buffer, 0, length);
                    }
                    zipOut.closeEntry();
                    inputStream.close();
                }
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                return new InputStreamResource(byteArrayInputStream);
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при загрузке файлов", e);
            }
    }

    private static void convertPrice(ResponseProductDto responseProduct, BigDecimal currency) {
        BigDecimal rubPrice = responseProduct.getPrice();
        responseProduct.setPrice(rubPrice.divide(currency, 2, RoundingMode.HALF_UP));
    }

    private void setCurrencyInProducts(List<ResponseProductDto> products, Currency currency) {
        products.forEach(p -> p.setCurrency(currency));
    }

}
