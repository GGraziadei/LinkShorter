package it.erroridiprezzo.ErroriDiPrezzoShort.services;

import it.erroridiprezzo.ErroriDiPrezzoShort.entities.Url;
import it.erroridiprezzo.ErroriDiPrezzoShort.entities.UrlStat;
import it.erroridiprezzo.ErroriDiPrezzoShort.repositories.UrlRepository;
import it.erroridiprezzo.ErroriDiPrezzoShort.repositories.UrlStatRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private UrlStatRepository urlStatRepository;

    public String generateReport( int days ) throws IOException {
        LocalDate today = LocalDate.now();
        List<UrlStat> urlStatList = this.urlStatRepository.findAll()
                .stream()
                .filter( s -> today.minusDays(days).isBefore(LocalDate.from(s.getTs())))
                .collect(Collectors.toList());
        String fileName = UUID.randomUUID().toString();
        File file = new File("src/main/resources/reports/" + fileName +".xls" );
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reports");

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("ID");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("URL");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("IP");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("USER_AGENT");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("TIME");
        headerCell.setCellStyle(headerStyle);

        AtomicInteger rowIndex = new AtomicInteger(1);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(false);


        urlStatList.forEach(
                stat -> {
                    Row row = sheet.createRow( rowIndex.addAndGet(1));

                    Cell cell = row.createCell(0);
                    cell.setCellValue(stat.getId());
                    cell.setCellStyle(style);

                    cell = row.createCell(1);
                    cell.setCellValue(stat.getUrl());
                    cell.setCellStyle(style);

                    cell = row.createCell(2);
                    cell.setCellValue(stat.getIp());
                    cell.setCellStyle(style);

                    cell = row.createCell(3);
                    cell.setCellValue(stat.getUserAgent());
                    cell.setCellStyle(style);

                    cell = row.createCell(4);
                    cell.setCellValue(stat.getTs().toString());
                    cell.setCellStyle(style);

                }
        );

        FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
        workbook.write(outputStream);
        workbook.close();

        return file.getAbsolutePath();
    }

    public String generateReport(int days, String url ) throws IOException {
        LocalDate today = LocalDate.now();

        List<UrlStat> urlStatList = this.urlStatRepository.findByUrl(url)
                .stream()
                .filter( s -> today.minusDays(days).isBefore(LocalDate.from(s.getTs())))
                //.filter( s -> s.getUrl().equals(url.getUrl()))
                .collect(Collectors.toList());
        String fileName = UUID.randomUUID().toString();
        File file = new File("src/main/resources/reports/" + fileName +".xls" );
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reports");

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("ID");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("URL");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("IP");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("USER_AGENT");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("TIME");
        headerCell.setCellStyle(headerStyle);

        AtomicInteger rowIndex = new AtomicInteger(1);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(false);


        urlStatList.forEach(
                stat -> {
                    Row row = sheet.createRow( rowIndex.addAndGet(1));

                    Cell cell = row.createCell(0);
                    cell.setCellValue(stat.getId());
                    cell.setCellStyle(style);

                    cell = row.createCell(1);
                    cell.setCellValue(stat.getUrl());
                    cell.setCellStyle(style);

                    cell = row.createCell(2);
                    cell.setCellValue(stat.getIp());
                    cell.setCellStyle(style);

                    cell = row.createCell(3);
                    cell.setCellValue(stat.getUserAgent());
                    cell.setCellStyle(style);

                    cell = row.createCell(4);
                    cell.setCellValue(stat.getTs().toString());
                    cell.setCellStyle(style);

                }
        );

        FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
        workbook.write(outputStream);
        workbook.close();

        return file.getAbsolutePath();
    }
}
