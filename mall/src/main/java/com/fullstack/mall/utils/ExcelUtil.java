package com.fullstack.mall.utils;


import org.apache.poi.ss.usermodel.*;

/**
 * 描述：     处理Excel
 */
public class ExcelUtil {
    public static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null; // 空单元格
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue(); // 处理日期
                }
                return cell.getNumericCellValue();
            case FORMULA:
                return cell.getCellFormula(); // 公式单元格
            case BLANK:
                return "";
            default:
                return null;
        }
    }
}
