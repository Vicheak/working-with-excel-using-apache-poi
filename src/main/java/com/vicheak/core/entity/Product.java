package com.vicheak.core.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @Column(name = "product_name", nullable = false, unique = true)
    private String name;
    @Column(name = "product_stock")
    private Integer availableStock;
    @Column(name = "product_sale_price")
    private BigDecimal salePrice;
    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;
    @OneToMany(mappedBy = "product")
    private List<ProductImportHistory> productImportHistories;

}
