package com.wangrj.java_lib.java_util.excel;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ExcelWriterTest {

    @Test
    public void testExcel() throws IOException {
        // 写
        ExcelWriter excelWriter = new ExcelWriter();
        excelWriter.setSheetName("SheetOne");
        excelWriter.setStart("B2");
        byte[] excelBytes = excelWriter.writeExcel(Arrays.asList(
                Arrays.asList(1.0, 10.5, "user_1", true),
                Arrays.asList(2.0, 20.5, "user_2", false)
        ));

        // 读
        ExcelReader excelReader = new ExcelReader();
        excelReader.setSheetName("SheetOne");
        excelReader.setSelectedArea("B2", "E3");
        List<List<Object>> valueLists = excelReader.readExcel(new ByteArrayInputStream(excelBytes));
        assertArrayEquals(new Object[]{1.0, 10.5, "user_1", true}, valueLists.get(0).toArray());
        assertArrayEquals(new Object[]{2.0, 20.5, "user_2", false}, valueLists.get(1).toArray());

        // 使用模版
        excelWriter = new ExcelWriter();
        excelWriter.setExcelTemplate(new ByteArrayInputStream(excelBytes));
        excelWriter.setStart("B5");
        excelWriter.writeExcel(Arrays.asList(
                Arrays.asList(1.0, "A"),
                Arrays.asList(2.0, "B")
        ));
    }

    @Test
    public void writeDate() throws IOException {
        ExcelWriter excelWriter = new ExcelWriter();
        excelWriter.setDateFormat("yyyy/MM/dd");
        byte[] bytes = excelWriter.writeExcel(Collections.singletonList(Collections.singletonList(new Date())));

        ExcelReader excelReader = new ExcelReader();
        List<List<Object>> valueLists = excelReader.readExcel(new ByteArrayInputStream(bytes));
        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")), valueLists.get(0).get(0));
    }

    @Test
    public void testXlsxFormat() throws IOException {
        // 以 XLSX 的格式保存
        ExcelWriter excelWriter = new ExcelWriter();
        excelWriter.setStart("A1");
        excelWriter.setExportType(ExcelWriter.ExportType.XLSX);
        byte[] excelBytes = excelWriter.writeExcel(Arrays.asList(
                Arrays.asList("1", "A"),
                Arrays.asList("2", "B")
        ));

        // 以 XLSX 的格式读取
        ExcelReader excelReader = new ExcelReader();
        excelReader.setSelectedArea("A1", "B2");
        List<List<Object>> valueLists = excelReader.readExcel(new ByteArrayInputStream(excelBytes));
        assertArrayEquals(new Object[]{"1", "A"}, valueLists.get(0).toArray());
        assertArrayEquals(new Object[]{"2", "B"}, valueLists.get(1).toArray());
    }

    @Test
    public void testNotSupportValueType() throws IOException {
        // 写
        ExcelWriter excelWriter = new ExcelWriter();
        try {
            excelWriter.writeExcel(Collections.singletonList(Collections.singletonList(1)));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("can not resolve type: Integer which value is: 1", e.getMessage());
        }
        try {
            excelWriter.writeExcel(Collections.singletonList(Collections.singletonList(1F)));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("can not resolve type: Float which value is: 1.0", e.getMessage());
        }
        LocalDateTime now = LocalDateTime.now();
        try {
            excelWriter.writeExcel(Collections.singletonList(Collections.singletonList(now)));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("can not resolve type: LocalDateTime which value is: " + now, e.getMessage());
        }
    }

    @Test
    public void parseCellNumber() {
        assertArrayEquals(new int[]{0, 0}, ExcelWriter.parseCellNumber("A1"));
        assertArrayEquals(new int[]{1, 0}, ExcelWriter.parseCellNumber("A2"));
        assertArrayEquals(new int[]{0, 1}, ExcelWriter.parseCellNumber("B1"));
        assertArrayEquals(new int[]{11, 25}, ExcelWriter.parseCellNumber("Z12"));
        assertArrayEquals(new int[]{20, 26}, ExcelWriter.parseCellNumber("AA21"));
        assertArrayEquals(new int[]{998, 26 * 2 + 25}, ExcelWriter.parseCellNumber("BZ999"));
        try {
            ExcelWriter.parseCellNumber("ABC123");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("cellNumber is invalid: ABC123", e.getMessage());
        }
    }

}