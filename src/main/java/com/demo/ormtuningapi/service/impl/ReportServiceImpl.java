package com.demo.ormtuningapi.service.impl;

import com.demo.ormtuningapi.model.Invoice;
import com.demo.ormtuningapi.model.InvoiceDetail;
import com.demo.ormtuningapi.repository.InvoiceRepository;
import com.demo.ormtuningapi.service.ReportService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author dperez
 */
@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {
    private static final int BATCH_SIZE = 10, NORMAL_QUERY_MODE = 1, FETCH_QUERY_MODE = 2, ENTITY_GRAPH_QUERY_MODE = 3;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###,###.000");


    private final InvoiceRepository invoiceRepository;

    @Async
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    @Override
    public CompletableFuture<byte[]> generateExcelReportForInvoices(String accountNumber, LocalDateTime startDate, LocalDateTime endDate, int queryMode) throws IOException {
        return generateExcelReport(accountNumber, startDate, endDate, queryMode);
    }

    public CompletableFuture<byte[]> generateExcelReport(String accountNumber, LocalDateTime startDate, LocalDateTime endDate, int queryMode) throws IOException {
        Objects.requireNonNull(accountNumber);
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);

        var slice = getInvoices(accountNumber, startDate, endDate, PageRequest.ofSize(BATCH_SIZE), queryMode);

        if (slice.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        try (final var xssfWorkbook = new XSSFWorkbook()) {
            final var invoicesSheet = createInvoicesSheet(xssfWorkbook);

            var invoices = slice.getContent();
            addInvoicesToReport(invoicesSheet, invoices, queryMode);

            while (slice.hasNext()) {
                slice = getInvoices(accountNumber, startDate, endDate, slice.nextPageable(), queryMode);
                invoices = slice.getContent();
                addInvoicesToReport(invoicesSheet, invoices, queryMode);
            }

            return CompletableFuture.completedFuture(generateReportFile(xssfWorkbook));
        }
    }

    private Slice<Invoice> getInvoices(String accountNumber, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable, int mode) {
        if (mode == NORMAL_QUERY_MODE) {
            return invoiceRepository.findInvoicesWithDetailsByAccountAndDateRange(accountNumber, startDate, endDate, pageable);
        }
        if (mode == FETCH_QUERY_MODE) {
            return invoiceRepository.findInvoicesWithDetailsByAccountAndDateRangeUsingJoinFetch(accountNumber, startDate, endDate, pageable);
        }
        if (mode == ENTITY_GRAPH_QUERY_MODE) {
            return invoiceRepository.findInvoicesWithDetailsByAccountAndDateRangeUsingEntityGraph(accountNumber, startDate, endDate, pageable);
        }
        throw new IllegalArgumentException();
    }

    private void addInvoicesToReport(Sheet sheet, List<Invoice> invoices, int queryMode) {
        invoices.forEach(invoice -> {
            addInvoicesValues(sheet, invoice);
            createInvoiceDetailsSheet(sheet.getWorkbook(), invoice.getNumber(), invoice.getDetails());
        });
    }

    private Sheet createInvoicesSheet(Workbook workbook) {
        var invoicesSheet = workbook.createSheet("Invoices");
        String[] columns = {"Invoice Number #", "Client Account", "Total", "Sub Total", "Taxes"};

        var headerRow = invoicesSheet.createRow(0);
        var titleCellStyle = getTitleStyle(workbook);
        for (var i = 0; i < columns.length; i++) {
            var cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(titleCellStyle);
        }
        return invoicesSheet;
    }

    private void addInvoicesValues(Sheet sheet, Invoice invoice) {
        var valuesRow = sheet.createRow(sheet.getLastRowNum() + 1);

        var invoiceNumber = valuesRow.createCell(0);
        invoiceNumber.setCellValue(invoice.getNumber());

        var clientAccountNumberCell = valuesRow.createCell(1);
        clientAccountNumberCell.setCellValue(invoice.getClientAccountNumber());

        var totalCell = valuesRow.createCell(2);
        totalCell.setCellValue(DECIMAL_FORMAT.format(invoice.getTotal()));

        var subTotalCell = valuesRow.createCell(3);
        subTotalCell.setCellValue(DECIMAL_FORMAT.format(invoice.getSubTotal()));

        var taxesCell = valuesRow.createCell(4);
        taxesCell.setCellValue(DECIMAL_FORMAT.format(invoice.getTaxes()));
    }


    private void createInvoiceDetailsSheet(Workbook workbook, String invoiceNumber, Set<InvoiceDetail> invoiceDetails) {
        var sheet = workbook.createSheet("Invoice Details #".concat(invoiceNumber));
        String[] columns = {"Product Name", "Quantity", "Price", "Total"};

        var headerRow = sheet.createRow(0);
        for (var i = 0; i < columns.length; i++) {
            var cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        invoiceDetails.forEach(invoiceDetail -> {
            var valuesRow = sheet.createRow(sheet.getLastRowNum() + 1);
            var productName = valuesRow.createCell(0);
            productName.setCellValue(invoiceDetail.getProductName());

            var quantityCell = valuesRow.createCell(1);
            quantityCell.setCellValue(invoiceDetail.getQuantity());

            var priceCell = valuesRow.createCell(2);
            priceCell.setCellValue(DECIMAL_FORMAT.format(invoiceDetail.getPrice()));

            var totalCell = valuesRow.createCell(3);
            totalCell.setCellValue(DECIMAL_FORMAT.format(invoiceDetail.getTotal()));
        });
    }

    public CellStyle getTitleStyle(Workbook workbook) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(font);
        return titleStyle;
    }

    private byte[] generateReportFile(Workbook workbook) throws IOException {
        try (var outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

}
