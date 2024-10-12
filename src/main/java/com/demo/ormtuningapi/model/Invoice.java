package com.demo.ormtuningapi.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author dperez
 */
@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "number", updatable = false, nullable = false)
    private String number;
    @Column(name = "total", nullable = false)
    private BigDecimal total;
    @Column(name = "sub_total", nullable = false)
    private BigDecimal subTotal;
    @Column(name = "taxes", nullable = false)
    private BigDecimal taxes;
    @Column(name = "client_account_number", nullable = false)
    private String clientAccountNumber;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InvoiceDetail> details;

}
