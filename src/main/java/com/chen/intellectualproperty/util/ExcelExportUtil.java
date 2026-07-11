package com.chen.intellectualproperty.util;

import com.chen.intellectualproperty.annotation.ExcelField;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ExcelExportUtil {

    /**
     * 将数据列表导出为Excel并写入HttpServletResponse
     *
     * @param dataList  数据列表
     * @param sheetName 工作表名称（同时作为下载文件名）
     * @param response  HttpServletResponse
     */
    public static <T> void exportToExcel(List<T> dataList, String sheetName, HttpServletResponse response)
            throws IOException {
        if (dataList == null || dataList.isEmpty()) {
            // 空数据，写入空表头
            try (Workbook workbook = new XSSFWorkbook()) {
                workbook.createSheet(sheetName);
                String fileName = URLEncoder.encode(sheetName, StandardCharsets.UTF_8)
                        .replace("+", "%20");
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + fileName + ".xlsx");
                try (OutputStream out = response.getOutputStream()) {
                    workbook.write(out);
                    out.flush();
                }
            }
            return;
        }
        Class<?> clazz = dataList.get(0).getClass();

        // 收集带有 @ExcelField 注解的字段，按order排序
        List<FieldEntry> fieldEntries = new ArrayList<>();
        if (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                ExcelField annotation = field.getAnnotation(ExcelField.class);
                if (annotation != null) {
                    field.setAccessible(true);
                    fieldEntries.add(new FieldEntry(field, annotation));
                }
            }
        }
        fieldEntries.sort(Comparator.comparingInt(e -> e.annotation.order()));

        // 创建工作簿
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);

            // 创建表头样式（加粗 + 背景色）
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // 日期样式
            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));

            // 写表头
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < fieldEntries.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(fieldEntries.get(i).annotation.value());
                cell.setCellStyle(headerStyle);
            }

            // 写数据行
            for (int rowIdx = 0; rowIdx < dataList.size(); rowIdx++) {
                Row row = sheet.createRow(rowIdx + 1);
                T data = dataList.get(rowIdx);
                for (int colIdx = 0; colIdx < fieldEntries.size(); colIdx++) {
                    FieldEntry entry = fieldEntries.get(colIdx);
                    Cell cell = row.createCell(colIdx);
                    try {
                        Object value = entry.field.get(data);
                        setCellValue(cell, value, entry.annotation, workbook, dateStyle);
                    } catch (IllegalAccessException e) {
                        cell.setCellValue("");
                    }
                }
            }

            // 自动调整列宽（限制最大宽度）
            for (int i = 0; i < fieldEntries.size(); i++) {
                sheet.autoSizeColumn(i);
                int width = sheet.getColumnWidth(i);
                if (width > 15000) {
                    sheet.setColumnWidth(i, 15000);
                }
            }

            // 设置响应头
            String fileName = URLEncoder.encode(sheetName, StandardCharsets.UTF_8)
                    .replace("+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + fileName + ".xlsx");

            // 写入输出流
            try (OutputStream out = response.getOutputStream()) {
                workbook.write(out);
                out.flush();
            }
        }
    }

    private static void setCellValue(Cell cell, Object value, ExcelField annotation,
                                      Workbook workbook, CellStyle dateStyle) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }
        if (value instanceof Date date) {
            cell.setCellValue(formatDate(date, annotation.dateFormat()));
            cell.setCellStyle(dateStyle);
        } else if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
        } else if (value instanceof Boolean bool) {
            cell.setCellValue(bool);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    private static String formatDate(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    private static class FieldEntry {
        final Field field;
        final ExcelField annotation;

        FieldEntry(Field field, ExcelField annotation) {
            this.field = field;
            this.annotation = annotation;
        }
    }
}
