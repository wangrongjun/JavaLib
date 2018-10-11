package com.wangrj.java_lib.java_util.excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * by robin.wang on 2018/8/26.
 */
public class ExcelReader {

    /**
     * 设置读取的excel文件的sheetName。如果不设置，则默认读取第一个sheet
     */
    private String sheetName;
    /**
     * 指定从表格中读取数据的起始位置。默认第一个单元格为起始位置。
     */
    private int startRow = 0;
    private int startColumn = 0;
    /**
     * 指定从表格中读取数据的结束位置。起始位置和结束位置决定了表格的一个矩形区域，只在这个区域内读取数据。
     */
    private Integer endRow = 0;
    private Integer endColumn = 0;

    /**
     * 从 Excel 文件的指定的矩形区域中读取数据并返回 二维数组
     * <p>
     * @return 值类型有且仅有以下类型：String, boolean, double, Date
     */
    public List<List<Object>> readExcel(InputStream excelFile) throws IOException {
        // 先备份输入流，以便第一次以 xls 的格式读取失败后，可以再接着以 xlsx 的格式读取一次
        // 因为 InputStream 读完一次到达EOF后就无法读第二次了
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = excelFile.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();

        Workbook workbook;
        try {
            workbook = new HSSFWorkbook(new ByteArrayInputStream(baos.toByteArray()));// 使用xls的格式读取
        } catch (OfficeXmlFileException e) {// 抛出该异常，说明传入的是xlsx格式的文件
            workbook = new XSSFWorkbook(new ByteArrayInputStream(baos.toByteArray()));// 使用xlsx的格式读取
        }
        baos.close();

        try {
            Sheet sheet;
            if (sheetName != null) {
                sheet = workbook.getSheet(sheetName);
            } else {
                sheet = workbook.getSheetAt(0);
            }

            List<List<Object>> valueLists = new ArrayList<>();
            for (int i = startRow; i <= endRow && i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                List<Object> valueList = new ArrayList<>();

                boolean existNotNullField = false;
                for (int j = startColumn; j <= endColumn && j <= row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    Object value;
                    switch (cell.getCellTypeEnum()) {
                        case NUMERIC:
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                value = cell.getDateCellValue();
                            } else {
                                value = cell.getNumericCellValue();
                            }
                            break;
                        case STRING:
                            value = cell.getStringCellValue();
                            break;
                        case BOOLEAN:
                            value = cell.getBooleanCellValue();
                            break;
                        case _NONE:
                        case BLANK:
                            value = null;
                            break;
                        default:
                            throw new RuntimeException("Can't parse data type " + cell.getCellTypeEnum() +
                                    " at (" + (row.getRowNum() + 1) + "," + (cell.getColumnIndex() + 1) + ")");
                    }

                    valueList.add(value);// 即使是value是null也要加，表明这一列为空，而非没有这一列
                    if (value != null) {
                        existNotNullField = true;
                    }
                }

                if (existNotNullField) {// 如果这一行的单元格全为空，就跳过。否则添加。
                    valueLists.add(valueList);
                }
            }

            return valueLists;

        } finally {
            workbook.close();
        }
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public void setSelectedArea(int startRow, int startColumn, int endRow, int endColumn) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.endRow = endRow;
        this.endColumn = endColumn;
    }

    /**
     * 通过设置单元格编号来指定读取数据的区域。比如start=A2，end=D4，就读取区域 A2:D4
     *
     * @see ExcelWriter#parseCellNumber(String)
     */
    public void setSelectedArea(String start, String end) {
        int[] startPos = ExcelWriter.parseCellNumber(start);
        int[] endPos = ExcelWriter.parseCellNumber(end);
        setSelectedArea(startPos[0], startPos[1], endPos[0], endPos[1]);
    }

}
