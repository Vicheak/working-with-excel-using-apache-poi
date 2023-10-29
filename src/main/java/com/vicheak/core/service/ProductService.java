package com.vicheak.core.service;

import com.vicheak.core.dto.ProductDto;
import com.vicheak.core.dto.ProductImportDto;
import com.vicheak.core.dto.SalePriceDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductService {

    void importProduct(ProductImportDto productImportDto);

    Map<Integer, String> uploadImportProducts(MultipartFile multipartFile) throws IOException;

    void setSalePrice(Long productId);

    List<ProductDto> loadProducts();

}
