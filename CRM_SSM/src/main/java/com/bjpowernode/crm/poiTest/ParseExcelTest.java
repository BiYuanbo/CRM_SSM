package com.bjpowernode.crm.poiTest;

import com.bjpowernode.crm.commons.utils.HSSFUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ParseExcelTest {
    public static void main(String[] args) throws IOException {
        InputStream is = new FileInputStream("D:\\IdeaProjects\\CRM\\studentList.xls");

        HSSFWorkbook hs = new HSSFWorkbook(is);

        HSSFSheet sheet = hs.getSheetAt(0);//页的下标。从零开始，依次增加

        HSSFRow row = null;
        HSSFCell cell = null;
        for (int i = 0; i <= sheet.getLastRowNum(); i++){//sheet.getLastRowNum()：获取该页最后一行的下标
            row = sheet.getRow(i);//行的下标。从零开始，依次增加

            for (int j = 0 ; j < row.getLastCellNum(); j++){
                cell = row.getCell(j);//列的下标。从零开始，依次增加

                System.out.print(HSSFUtils.getCellValue(cell)+" ");
            }
            System.out.println();
        }
    }

}
