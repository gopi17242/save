package com.mail;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVReader;

class Test {
	
	static String todayDate ="";
    public static void main(String[] args) throws IOException {
    	
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		System.out.println(dtf.format(localDate)); //2016/11/16
		todayDate = dtf.format(localDate);
		
		XSSFWorkbook wb = new XSSFWorkbook();
        CreationHelper helper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("sheet1");

        CSVReader reader = new CSVReader(new FileReader("D:\\"+todayDate+"\\small-countries\\afghanistan.csv"));
        String[] line;
        int r = 0;
        while ((line = reader.readNext()) != null) {
            Row row = sheet.createRow((short) r++);

            for (int i = 0; i < line.length; i++)
                row.createCell(i)
                   .setCellValue(helper.createRichTextString(line[i]));
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("D:\\"+todayDate+"\\workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }
}