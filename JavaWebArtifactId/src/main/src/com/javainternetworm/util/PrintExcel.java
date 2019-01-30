package com.javainternetworm.util;

import com.bean.Movie;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class PrintExcel {
    String filePath = "/Users/lubin/Desktop/top250.xls";
    String sheetName = "sheet1";

    public void createTop250(){
        String[] titleRow = new String[]{"排名","名称","导演","编剧","国家","年份","时长","分数","人数"};
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);

        FileOutputStream out = null;
        try {
            HSSFRow row = workbook.getSheet(sheetName).createRow(0);
            for (int i=0;i<titleRow.length;i++){
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(titleRow[i]);
            }
            out = new FileOutputStream(filePath);
            workbook.write(out);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                out.close();
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }
    }

     public void printTop250(List<Movie> movies){
         File file = new File(filePath);
         HSSFWorkbook workbook = null;
         try {
              workbook = new HSSFWorkbook(new FileInputStream(file));
         }catch (Exception e){
             e.printStackTrace();
         }

         FileOutputStream out = null;
         HSSFSheet sheet = workbook.getSheet(sheetName);
         int columnCount = sheet.getRow(0).getLastCellNum()+1;
         try {
             HSSFRow titlwRow = sheet.getRow(0);
             if (titlwRow != null){
                 Movie movie;
                 for (int rowId = 0;rowId<movies.size();rowId++){
                     movie = movies.get(rowId);
                     HSSFRow newRow = sheet.createRow(rowId+1);
                     newRow.createCell(0).setCellValue(movie.getRank());
                     newRow.createCell(1).setCellValue(movie.getName());
                     newRow.createCell(2).setCellValue(movie.getDirector());
                     newRow.createCell(3).setCellValue(movie.getScreenWriter());
                     newRow.createCell(4).setCellValue(movie.getCountry());
                     newRow.createCell(5).setCellValue(movie.getYear());
                     newRow.createCell(6).setCellValue(movie.getDuration());
                     newRow.createCell(7).setCellValue(movie.getScore());
                     newRow.createCell(8).setCellValue(movie.getPeopleNum());
                 }
             }
             out =  new FileOutputStream(filePath);
             workbook.write(out);
         }catch (Exception e){
             e.printStackTrace();
         }finally {
             try {
                 out.close();
             }catch (Exception e){
                 e.printStackTrace();
             }
         }
     }

}
