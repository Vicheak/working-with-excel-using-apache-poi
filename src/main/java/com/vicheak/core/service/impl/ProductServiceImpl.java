package com.vicheak.core.service.impl;

import com.vicheak.core.dto.ProductDto;
import com.vicheak.core.dto.ProductImportDto;
import com.vicheak.core.dto.UpdateProductDto;
import com.vicheak.core.entity.Color;
import com.vicheak.core.entity.Product;
import com.vicheak.core.entity.ProductImportHistory;
import com.vicheak.core.mapper.ProductImportHistoryMapper;
import com.vicheak.core.mapper.ProductMapper;
import com.vicheak.core.repository.ColorRepository;
import com.vicheak.core.repository.ProductImportHistoryRepository;
import com.vicheak.core.repository.ProductRepository;
import com.vicheak.core.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductImportHistoryRepository productImportHistoryRepository;
    private final ProductImportHistoryMapper productImportHistoryMapper;
    private final ColorRepository colorRepository;

    @Transactional
    @Override
    public void importProduct(ProductImportDto productImportDto) {
        //check product if exists
        Product product = productRepository.findById(productImportDto.productId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Product with id = %d not found",
                                        productImportDto.productId()))
                );

        //update product stock
        Integer availableStock = product.getAvailableStock() == null ? 0 : product.getAvailableStock();
        product.setAvailableStock(availableStock + productImportDto.importUnit());
        productRepository.save(product);

        //save product import history
        ProductImportHistory productImportHistory = productImportHistoryMapper.toProductImportHistory(productImportDto);
        productImportHistoryRepository.save(productImportHistory);
    }

    @Transactional
    @Override
    public Map<Integer, String> uploadImportProducts(MultipartFile multipartFile) throws IOException {
        //map to customize exception from excel file
        Map<Integer, String> exceptions = new HashMap<>();

        Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        Sheet sheet = workbook.getSheet("import_sheet");
        Iterator<Row> rowIterator = sheet.iterator();

        //skip row header
        while (rowIterator.hasNext()) {
            rowIterator.next();
            break;
        }

        //access each cell within each row
        while (rowIterator.hasNext()) {
            Integer rowNumber = 0;
            try {
                int columnIndex = 0;
                Row row = rowIterator.next();
                Cell rowNumberCell = row.getCell(columnIndex++);
                rowNumber = (int) rowNumberCell.getNumericCellValue();
                if (rowNumber <= 0) {
                    rowNumber = -1;
                    throw new Exception("Row Number must be greater than 0");
                }

                Cell productIdCell = row.getCell(columnIndex++);
                Long productId = (long) productIdCell.getNumericCellValue();
                if (productId <= 0) {
                    throw new Exception("Product ID must be greater than 0");
                }

                Cell importDateCell = row.getCell(columnIndex++);
                LocalDateTime importDate = importDateCell.getLocalDateTimeCellValue();

                Cell importUnitCell = row.getCell(columnIndex++);
                Integer importUnit = (int) importUnitCell.getNumericCellValue();
                if (importUnit <= 0) {
                    throw new Exception("Import Unit must be greater than 0");
                }

                Cell importPriceCell = row.getCell(columnIndex);
                BigDecimal importPrice = BigDecimal.valueOf(importPriceCell.getNumericCellValue());
                if (importPrice.compareTo(BigDecimal.valueOf(0)) <= 0) {
                    throw new Exception("Import Price must be greater than 0");
                }

                //pass to product import dto
                ProductImportDto productImportDto = new ProductImportDto(productId,
                        importDate, importUnit, importPrice);

                this.importProduct(productImportDto);
            } catch (Exception ex) {
                exceptions.put(rowNumber, ex.getMessage());
            }
        }
        return exceptions;
    }

    @Transactional
    @Override
    public void setSalePrice(Long productId) {
        //check product if exists
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Product with id = %d not found",
                                        productId))
                );

        //load product import histories by product id
        List<ProductImportHistory> productImportHistories = productImportHistoryRepository.findByProductId(productId);
        if (productImportHistories.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No imported product yet! cannot set sale price");
        }

        List<ProductImportDto> productImportDtos = productImportHistoryMapper.toProductImportDto(productImportHistories);

        Optional<BigDecimal> maxPrice = productImportDtos.stream()
                .map(ProductImportDto::importPrice)
                .max((price1, price2) -> Integer.compare(price1.subtract(price2).compareTo(BigDecimal.valueOf(0)), 0));

        BigDecimal finalSalePrice = BigDecimal.valueOf(0);
        if (maxPrice.isPresent()) {
            finalSalePrice = maxPrice.get();
        }

        //update the product and add 200$ to imported price
        product.setSalePrice(finalSalePrice.add(BigDecimal.valueOf(200)));
        productRepository.save(product);
    }

    @Override
    public List<ProductDto> loadProducts() {
        return productMapper.toProductDto(productRepository.findAll());
    }

    @Override
    public void updateById(Long productId, UpdateProductDto updateProductDto) {
        //check product if exists
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Product with id = %d not found",
                                        productId))
                );

        productMapper.fromUpdateProductDtoToProduct(product, updateProductDto);

        //update the reference
        if (updateProductDto.colorId() != null) {
            //check color if exists
            colorRepository.findById(updateProductDto.colorId())
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    String.format("Color with id = %d not found",
                                            updateProductDto.colorId()))
                    );

            Color color = new Color();
            color.setId(updateProductDto.colorId());
            product.setColor(color);
        }

        productRepository.save(product);
    }

}
