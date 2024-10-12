package com.demo.ormtuningapi.controller;

import com.demo.ormtuningapi.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

/**
 * @author dperez
 */
@RestController
@RequestMapping("reports")
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/invoices/excel")
    public CompletableFuture<ResponseEntity<byte[]>> generateInvoicesExcel(@RequestParam("accountNumber") String accountNumber,
                                                                           @RequestParam(value = "startDate", required = false) LocalDateTime startDate,
                                                                           @RequestParam(value = "endDate", required = false) LocalDateTime endDate,
                                                                           @RequestParam(value = "queryMode") int queryMode) throws IOException {

        if (startDate == null || endDate == null) {
            startDate = LocalDate.now().minusDays(5).atStartOfDay();
            endDate = LocalDate.now().atTime(LocalTime.MAX);
        }

        return reportService.generateExcelReportForInvoices(accountNumber, startDate, endDate, queryMode).thenApply(reportBytes -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=invoices_report.xlsx");
            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
        });
    }

}

