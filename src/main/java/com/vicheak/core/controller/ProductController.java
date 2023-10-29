package com.vicheak.core.controller;

import com.vicheak.core.dto.ProductDto;
import com.vicheak.core.dto.ProductImportDto;
import com.vicheak.core.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/importProduct")
    public void importProduct(@RequestBody @Valid ProductImportDto productImportDto) {
        productService.importProduct(productImportDto);
    }

    @PostMapping("/uploadImportProducts")
    public ResponseEntity<?> uploadImportProducts(@RequestPart MultipartFile file) throws IOException {
        Map<Integer, String> exceptions = productService.uploadImportProducts(file);
        if (exceptions.isEmpty()) {
            return new ResponseEntity<>(Map.of(1, "Upload Import Products Successfully!"),
                    HttpStatus.CREATED);
        }
        return new ResponseEntity<>(exceptions, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{productId}/setSalePrice")
    public void setSalePrice(@PathVariable("productId") Long productId) {
        productService.setSalePrice(productId);
    }

    @GetMapping
    public List<ProductDto> loadProducts() {
        return productService.loadProducts();
    }

}
