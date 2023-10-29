package com.vicheak.core.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "product_import_histories")
public class ProductImportHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "import_id")
    private Long id;
    @Column(name = "import_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime importDate;
    @Column(name = "import_unit", nullable = false)
    private Integer importUnit;
    @Column(name = "import_price", nullable = false)
    private BigDecimal importPrice;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
