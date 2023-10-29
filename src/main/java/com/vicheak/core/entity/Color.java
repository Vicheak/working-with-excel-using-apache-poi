package com.vicheak.core.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "colors")
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "color_id")
    private Long id;
    @Column(name = "color_name", nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "color")
    private List<Product> products;

}
