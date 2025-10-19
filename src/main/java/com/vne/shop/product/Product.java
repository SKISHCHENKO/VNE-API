package com.vne.shop.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
@Schema(description = "Полная модель товара")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "1")
    private Long id;

    @NotBlank
    @Schema(example = "VNE Cargo Pants")
    private String name;

    @Column(length = 4000)
    @Schema(example = "Techwear cargo pants with waterproof fabric")
    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Schema(example = "129.90")
    private BigDecimal price;

    @NotBlank
    @Schema(example = "pants")
    private String category;

    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size")
    @Schema(example = "[\"S\",\"M\",\"L\"]")
    private List<String> sizes = new ArrayList<>();
}
