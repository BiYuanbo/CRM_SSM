package com.bjpowernode.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class HSSFUtils {
    //从指定HSSFSheet对象中获取列的值
    public static String getCellValue(HSSFCell cell){
        String result = "";
        //获取列中的数据
        if (cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
            result = cell.getStringCellValue();
        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
            result = cell.getNumericCellValue()+"";
        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
            result = cell.getBooleanCellValue()+"";
        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
            result = cell.getCellFormula()+"";
        }else {
            result = "";
        }

        return result;
    }
}
