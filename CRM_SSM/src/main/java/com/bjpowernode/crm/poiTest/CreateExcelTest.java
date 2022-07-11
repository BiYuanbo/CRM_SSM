package com.bjpowernode.crm.poiTest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/*
* 使用apache-poi生成excel文件*/
public class CreateExcelTest {
    public static void main(String[] args) {
        //创建HSSFWorkbook对象，对应一个excel文件
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        //使用HSSFWorkbook对象创建HSSFSheet对象，对应HSSFWorkbook对象文件中的一页
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("学生列表");
        //使用hssfSheet创建HssfRow对象，对应hssfSheet中的一行
        HSSFRow row = hssfSheet.createRow(0);//行号，从零开始，依次增加
        //使用row创建HssfCell对象，对应row中的一列
        HSSFCell cell = row.createCell(0);//列号，从零开始，依次增加
        cell.setCellValue("学号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("年龄");

        //使用hssfSheet创建十个HSSFRow对象,对应hssfSheet中的十行
        for (int i = 1; i <= 10; i++){
            row = hssfSheet.createRow(i);

            cell = row.createCell(0);
            cell.setCellValue(100+i);
            cell = row.createCell(1);
            cell.setCellValue("bb"+i);
            cell = row.createCell(2);
            cell.setCellValue(10+i);
        }

        //调用工具函数生成excel文件
        OutputStream os = null;
        try {
            os = new FileOutputStream("D:\\IdeaProjects\\CRM\\CRM_SSM\\studentList.xls");
            hssfWorkbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                hssfWorkbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("================OK================");
    }
}
