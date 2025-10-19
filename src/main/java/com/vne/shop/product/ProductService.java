package com.vne.shop.product;

import com.vne.shop.common.NotFoundException;
import com.vne.shop.product.dto.ProductCreateRequest;
import com.vne.shop.product.dto.ProductListItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository repo;

    @Transactional(readOnly = true)
    public List<ProductListItemDTO> list(String category, String q) {

        category = normalize(category);
        q = normalize(q);

        List<Product> products;
        if (hasText(category) && hasText(q)) {
            products = repo.findByCategoryIgnoreCaseContainingAndNameContainingIgnoreCase(category, q);
        } else if (hasText(category)) {
            products = repo.findByCategoryIgnoreCaseContaining(category);
        } else if (hasText(q)) {
            products = repo.findByNameContainingIgnoreCase(q);
        } else {
            products = repo.findAll();
        }

        return products.stream()
                .map(p -> new ProductListItemDTO(p.getId(), p.getName(), p.getPrice(), p.getCategory()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Product get(long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Товар %d не найден".formatted(id)));
    }

    public Product create(ProductCreateRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("Тело запроса не может быть пустое");
        }

        String name = normalize(req.name());
        String category = normalize(req.category());
        BigDecimal price = req.price();

        if (!hasText(name)) throw new IllegalArgumentException("Пропущено название товара!");
        if (!hasText(category)) throw new IllegalArgumentException("Пропущена категория товара!");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("цена должна быть больше > 0");


        List<String> sizes = req.sizes() == null ? List.of() :
                new java.util.ArrayList<>(
                        new LinkedHashSet<>(
                                req.sizes().stream()
                                        .filter(Objects::nonNull)
                                        .map(this::normalize)
                                        .filter(this::hasText)
                                        .toList()
                        )
                );

        Product p = new Product();
        p.setName(name);
        p.setDescription(normalize(req.description()));
        p.setPrice(price);
        p.setCategory(category);
        p.setSizes(sizes);

        return repo.save(p);
    }

    public void delete(long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Товар %d не найден".formatted(id));
        }
        repo.deleteById(id);
    }


    private String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private boolean hasText(String s) {
        return s != null && !s.isBlank();
    }
}
