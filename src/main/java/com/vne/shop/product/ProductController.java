package com.vne.shop.product;

import com.vne.shop.product.dto.ProductCreateRequest;
import com.vne.shop.product.dto.ProductListItemDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Products", description = "Каталог товаров VNE")
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    // GET /products?category=...&q=...
    @GetMapping
    @Operation(
            summary = "Список товаров",
            description = "Возвращает список товаров с возможностью поиска по категории и названию"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "ОК",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductListItemDTO.class)),
                            examples = @ExampleObject(
                                    value = "[{\n" +
                                            "  \"id\": 1, \"name\": \"VNE Cargo Pants\", \"price\": 129.90, \"category\": \"pants\"\n" +
                                            "}]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.vne.shop.common.ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n  \"error\": \"validation_error\", \"message\": \"Invalid 'category' or 'q'\"\n}"
                            )
                    )
            )
    })
    public List<ProductListItemDTO> list(
            @Parameter(
                    description = "Категория для фильтрации (частичное совпадение, регистр не учитывается)",
                    example = "pants"
            )
            @RequestParam(required = false) String category,

            @Parameter(
                    name = "q",
                    description = "Поиск по названию (частичное совпадение, регистр не учитывается)",
                    example = "cargo"
            )
            @RequestParam(required = false, name = "q") String q
    ) {
        return service.list(category, q);
    }

    // GET /products/{id}
    @GetMapping("/{id}")
    @Operation(
            summary = "Получить товар",
            description = "Возвращает полную информацию о товаре по его идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "ОК",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Product.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"id\": 1,\n" +
                                            "  \"name\": \"VNE Cargo Pants\",\n" +
                                            "  \"description\": \"Techwear cargo pants with waterproof fabric\",\n" +
                                            "  \"price\": 129.90,\n" +
                                            "  \"category\": \"pants\",\n" +
                                            "  \"sizes\": [\"S\",\"M\",\"L\"]\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Не найдено",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.vne.shop.common.ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n  \"error\": \"not_found\", \"message\": \"Product 999 not found\"\n}"
                            )
                    )
            )
    })
    public Product get(
            @Parameter(description = "Идентификатор товара", example = "1")
            @PathVariable long id
    ) {
        return service.get(id);
    }

    // POST /products
    @Operation(
            summary = "Создать товар",
            description = "Создаёт новый товар",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Тело запроса на создание товара",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.vne.shop.product.dto.ProductCreateRequest.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"name\": \"VNE Hoodie\",\n" +
                                            "  \"description\": \"Heavyweight fleece hoodie\",\n" +
                                            "  \"price\": 89.90,\n" +
                                            "  \"category\": \"tops\",\n" +
                                            "  \"sizes\": [\"S\",\"M\",\"L\",\"XL\"]\n" +
                                            "}"
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Создано",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.vne.shop.product.Product.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"id\": 4,\n" +
                                            "  \"name\": \"VNE Hoodie\",\n" +
                                            "  \"description\": \"Heavyweight fleece hoodie\",\n" +
                                            "  \"price\": 89.90,\n" +
                                            "  \"category\": \"tops\",\n" +
                                            "  \"sizes\": [\"S\",\"M\",\"L\",\"XL\"]\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Ошибка валидации",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.vne.shop.common.ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n  \"error\": \"validation_error\", \"message\": \"create.req.name: must not be blank\"\n}"
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<com.vne.shop.product.Product> create(
            @Valid
            @org.springframework.web.bind.annotation.RequestBody ProductCreateRequest req
    ) {
        var saved = service.create(req);
        return ResponseEntity
                .created(URI.create("/products/" + saved.getId()))
                .body(saved);
    }

    // DELETE /products/{id}
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить товар",
            description = "Удаляет товар по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Удалено"),
            @ApiResponse(
                    responseCode = "404", description = "Не найдено",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.vne.shop.common.ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n  \"error\": \"not_found\", \"message\": \"Product 999 not found\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Идентификатор товара", example = "3")
            @PathVariable long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
