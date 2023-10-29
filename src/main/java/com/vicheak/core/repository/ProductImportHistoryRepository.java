package com.vicheak.core.repository;

import com.vicheak.core.entity.ProductImportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImportHistoryRepository extends JpaRepository<ProductImportHistory, Long> {

    List<ProductImportHistory> findByProductId(Long productId);

}
