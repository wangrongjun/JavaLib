package com.wangrj.java_lib.java_util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * by robin.wang on 2018/8/26.
 */
public class ExcelWriter {

    public enum ExportType {XLS, XLSX}

    private ExportType exportType = ExportType.XLS;
    /**
     * 导出的Excel模版。一般会在模版中定义好表头，样式等。目前只支持xls格式，不支持xlsx格式。
     * <p>
     * TODO 支持xlsx格式
     */
    private InputStream excelTemplate;
    /**
     * 设置导出的excel文件的sheetName。如果不设置，则为 Sheet0
     */
    private String sheetName;
    /**
     * 指定数据填充到表格的起始单元格
     */
    private int startRow = 0;
    private int startColumn = 0;
    /**
     * 设置日期类型数据在Excel的显示格式，默认是 yyyy-MM-dd HH:mm:ss
     */
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 把 二维数组 中的数据写到Excel文件的 指定的起始位置 并导出Excel文件
     *
     * @param valueLists 值类型仅支持以下类型：String, boolean, double, Date
     */
    public void writeExcel(List<List<Object>> valueLists, OutputStream os) throws IOException {
        if (valueLists == null) {
            throw new NullPointerException("valueLists is null");
        }

        Workbook workbook;
        switch (exportType) {
            case XLS:
            default:// default是为了保险，即使exportType==null也不出错
                if (excelTemplate != null) {
                    workbook = new HSSFWorkbook(excelTemplate);
                } else {
                    workbook = new HSSFWorkbook();
                }
                break;
            case XLSX:
                if (excelTemplate != null) {
                    workbook = new XSSFWorkbook(excelTemplate);
                } else {
                    workbook = new XSSFWorkbook();
                }
                break;
        }

        try {
            Sheet sheet;
            if (sheetName != null) {
                if (excelTemplate != null) {
                    sheet = workbook.getSheet(sheetName);
                } else {
                    sheet = workbook.createSheet(sheetName);
                }
            } else {
                if (excelTemplate != null) {
                    sheet = workbook.getSheetAt(0);
                } else {
                    sheet = workbook.createSheet();
                }
            }

            for (int i = 0; i < valueLists.size(); i++) {// i指向每个不同的valueList，相当于表格的行
                List valueList = valueLists.get(i);
                Row row = null;
                if (excelTemplate != null) {
                    row = sheet.getRow(i + startRow);// 尝试先获取，获取不到，再创建。这样的好处是如果使用了template，会保留样式。
                }
                if (row == null) {
                    row = sheet.createRow(i + startRow);
                }

                for (int j = 0; j < valueList.size(); j++) {// j指向这个valueList的某一个value，相当于表格中的列
                    Cell cell = null;
                    if (excelTemplate != null) {
                        cell = row.getCell(j + startColumn);// 与Row同理
                    }
                    if (cell == null) {
                        cell = row.createCell(j + startColumn);
                    }

                    Object value = valueList.get(j);

                    if (value == null) {// 避免值为空，表格中却出现null字符串
                        continue;
                    }

                    switch (value.getClass().getSimpleName()) {
//                        case "int":
//                        case "Integer":
//                            cell.setCellValue((int) value);
//                            break;
//                        case "long":
//                        case "Long":
//                            cell.setCellValue((long) value);
//                            break;
//                        case "float":
//                        case "Float":
//                            cell.setCellValue((float) value);
//                            break;
                        case "double":
                        case "Double":
                            cell.setCellValue((double) value);
                            break;
                        case "boolean":
                        case "Boolean":
                            cell.setCellValue((boolean) value);
                            break;
                        case "String":
                            cell.setCellValue((String) value);
                            break;
                        case "Date":
//                            cell.setCellValue((Date) value);// 直接设置，Excel会会显示一个数字，而非日期。因为ApachePOI对日期的支持不是很好。
                            cell.setCellValue(new SimpleDateFormat(dateFormat).format((Date) value));
                            break;
                        default:
                            throw new IllegalArgumentException("can not resolve type: " +
                                    value.getClass().getSimpleName() + " which value is: " + value);
                    }
                }
            }

            workbook.write(os);
            os.flush();

        } finally {
            workbook.close();
        }
    }

    public byte[] writeExcel(List<List<Object>> valueLists) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeExcel(valueLists, baos);
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }

    public void setExportType(ExportType exportType) {
        this.exportType = exportType;
    }

    public void setExcelTemplate(InputStream excelTemplate) {
        this.excelTemplate = excelTemplate;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public void setStart(int rowOffset, int columnOffset) {
        this.startRow = rowOffset;
        this.startColumn = columnOffset;
    }

    /**
     * 通过设置单元格编号来指定数据填充开始区域。比如cellNumber=B2，就从B2单元格开始填充
     *
     * @see ExcelWriter#parseCellNumber(String)
     */
    public void setStart(String cellNumber) {
        int[] result = parseCellNumber(cellNumber);
        setStart(result[0], result[1]);
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * 通过单元格编号转换为行号和列号。比如cellNumber=A2，就返回[1,0]
     * <p>
     * B2 -> [1,1]
     * AA3 -> [2,26]
     * A23 -> [22,0]
     */
    public static int[] parseCellNumber(String cellNumber) {
        String regex = "^([a-zA-Z]{1,2})(\\d+)$";
        if (!cellNumber.matches(regex)) {
            throw new IllegalArgumentException("cellNumber is invalid: " + cellNumber);
        }
        Matcher matcher = Pattern.compile(regex).matcher(cellNumber);
        if (!matcher.find()) {
            throw new IllegalStateException("regex is error");
        }

        String column = matcher.group(1);
        String row = matcher.group(2);

        int columnIndex;
        if (column.length() == 1) {
            columnIndex = column.charAt(0) - 'A';
        } else {// length == 2
            columnIndex = (column.charAt(0) - 'A' + 1) * 26 + column.charAt(1) - 'A';
        }

        return new int[]{Integer.parseInt(row) - 1, columnIndex};
    }

}
