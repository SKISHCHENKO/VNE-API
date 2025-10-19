package com.vne.shop.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание товара")
public record ProductCreateRequest(
        @NotBlank @Schema(example = "VNE Hoodie") String name,
        @Schema(example = "Heavyweight fleece hoodie") String description,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) @Schema(example = "89.90") BigDecimal price,
        @NotBlank @Schema(example = "tops") String category,
        @Schema(example = "[\"S\",\"M\",\"L\",\"XL\"]") List<String> sizes
) {}
