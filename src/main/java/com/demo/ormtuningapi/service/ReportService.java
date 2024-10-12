package com.demo.ormtuningapi.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * @author dperez
 */
public interface ReportService {

    CompletableFuture<byte[]> generateExcelReportForInvoices(String accountNumber, LocalDateTime startDate, LocalDateTime endDate, int queryMode) throws IOException;

}
