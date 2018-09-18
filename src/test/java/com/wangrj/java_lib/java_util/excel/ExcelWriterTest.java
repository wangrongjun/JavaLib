package com.wangrj.java_lib.java_util.excel;


import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class ExcelWriterTest {

    @Test
    public void testExcel() throws IOException {
        // 写
        ExcelWriter excelWriter = new ExcelWriter();
        excelWriter.setSheetName("SheetOne");
        excelWriter.setStart("B2");
        byte[] excelBytes = excelWriter.writeExcel(Arrays.asList(
                Arrays.asList(1, 10.5, "user_1", true),
                Arrays.asList(2, 20.5, "user_2", false)
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
                Arrays.asList(1, "A"),
                Arrays.asList(2, "B")
        ));
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

}