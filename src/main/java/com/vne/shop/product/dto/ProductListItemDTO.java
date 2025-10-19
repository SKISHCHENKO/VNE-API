package com.vne.shop.product.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Краткая карточка товара в списке")
public record ProductListItemDTO(@Schema(example = "1") Long id, @Schema(example = "VNE Cargo Pants") String name, @Schema(example = "129.90") BigDecimal price, @Schema(example = "pants") String category) {}
