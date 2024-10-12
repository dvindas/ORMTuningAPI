package com.demo.ormtuningapi.repository;

import com.demo.ormtuningapi.model.Invoice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dperez
 */
@Repository
public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long> {

    @Query("SELECT i FROM Invoice i WHERE i.clientAccountNumber = :accountNumber AND i.createdAt BETWEEN :startDate AND :endDate")
    Slice<Invoice> findInvoicesWithDetailsByAccountAndDateRange(
            @Param("accountNumber") String accountNumber,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("SELECT i FROM Invoice i JOIN FETCH i.details WHERE i.clientAccountNumber = :accountNumber AND i.createdAt BETWEEN :startDate AND :endDate")
    Slice<Invoice> findInvoicesWithDetailsByAccountAndDateRangeUsingJoinFetch(
            @Param("accountNumber") String accountNumber,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"details"})
    @Query("SELECT i FROM Invoice i WHERE i.clientAccountNumber = :accountNumber AND i.createdAt BETWEEN :startDate AND :endDate")
    Slice<Invoice> findInvoicesWithDetailsByAccountAndDateRangeUsingEntityGraph(
            @Param("accountNumber") String accountNumber,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

}
