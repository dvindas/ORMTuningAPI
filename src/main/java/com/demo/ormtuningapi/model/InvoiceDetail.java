package com.demo.ormtuningapi.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author dperez
 */
@Entity
@Table(name = "invoice_details")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class InvoiceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Column(name = "total", nullable = false)
    private BigDecimal total;
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

}
