package com.jjc.excel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

public class ExcelExport {
    
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void export(String sheetName, String[] header, List<Object[]> datas, File file) throws IOException {
        try(HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet(sheetName);
            HSSFRow row = sheet.createRow(0);
            
            HSSFCellStyle styleHeader = workbook.createCellStyle();
            styleHeader.setAlignment(HorizontalAlignment.CENTER);
            
            HSSFFont fontHeader = workbook.createFont();
            fontHeader.setBold(true);
            styleHeader.setFont(fontHeader);
            
            for (int i = 0; i < header.length; i ++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(header[i]);
                cell.setCellStyle(styleHeader);
            }
            
            for (int i = 0; i < datas.size(); i++) {
                row = sheet.createRow(i + 1);
                Object[] objs = datas.get(i);
                for (int j = 0; j < objs.length; j++) {
                    Object data = objs[j];
                    if(data == null || data == "") {
                        row.createCell(j).setCellValue("");
                    }
                    else if(data instanceof Date) {
                        row.createCell(j).setCellValue(simpleDateFormat.format(data));
                    }
                    else if(data instanceof Integer) {
                        row.createCell(j).setCellValue((Integer) data);
                    }
                    else if(data instanceof Double) {
                        row.createCell(j).setCellValue((Double) data);
                    }
                    else if(data instanceof Long) {
                        row.createCell(j).setCellValue((Long) data);
                    }
                    else {
                        row.createCell(j).setCellValue(data.toString());
                    }
                }
            }
            workbook.write(file);
        }
    }
    
    public static void main(String[] args) throws IOException {
        String [] header = {"标题一", "标题二","标题三", "标题四"};
        
        List<Object[]> datas = new ArrayList<Object[]>();
        Object [] data = {"1", "2","3", 4};
        datas.add(data);
        
        export("导入", header, datas, new File("D://text.xls"));
    }
}
