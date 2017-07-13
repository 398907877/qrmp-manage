package com.yanyan.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;

/**
 * User: Saintcy
 * Date: 2015/5/31
 * Time: 9:09
 */
public class ExcelHelper {
    /**
     * 获取单元格的字符串值
     *
     * @param cell
     * @return
     */
    public static final String readStringCell(Cell cell) {
        String cellValue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    cellValue = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                    break;
                /*case Cell.CELL_TYPE_STRING:
                    cellValue = String.valueOf(cell.getStringCellValue());break;
                case Cell.CELL_TYPE_FORMULA:
                    try {
                        cellValue = String.valueOf(cell.getStringCellValue());
                    } catch (IllegalStateException e) {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_BLANK:
                    cellValue = "";break;*/
                case Cell.CELL_TYPE_BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    cellValue = String.valueOf(cell.getErrorCellValue());
                    break;
                default:
                    cellValue = cell.getStringCellValue();
            }
            return StringUtils.trimToEmpty(cellValue);
        } else {
            return "";
        }
    }

    public static void writeStringCell(Cell cell, Object o) {
        cell.setCellValue(new HSSFRichTextString(o == null ? "" : o.toString()));
    }
}
