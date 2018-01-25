package com.mail;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;


public class CSVToExcelConverter {
	static String todayDate ="";
	
	static File[] allexcelFiles;
	public static void convertingCSVtoEXCEL(String csvFileName) throws IOException {
		

    	
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		System.out.println(dtf.format(localDate)); //2016/11/16
		todayDate = dtf.format(localDate);
        ArrayList arList=null;
        ArrayList al=null;
        String fName = "D:\\small-countries\\"+csvFileName+".csv";
        String thisLine;
        int count=0;
        FileInputStream fis = new FileInputStream(fName);
        DataInputStream myInput = new DataInputStream(fis);
        int i=0;
        arList = new ArrayList();
        while ((thisLine = myInput.readLine()) != null)
        {
            al = new ArrayList();
            String strar[] = thisLine.split(",");
            for(int j=0;j<strar.length;j++)
            {
                al.add(strar[j]);
            }
            arList.add(al);
            System.out.println();
            i++;
        }

        try
        {
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("sheet1");
            for(int k=0;k<arList.size();k++)
            {
                ArrayList ardata = (ArrayList)arList.get(k);
                HSSFRow row = sheet.createRow((short) 0+k);
                for(int p=0;p<ardata.size();p++)
                {
                    HSSFCell cell = row.createCell((short) p);
                    String data = ardata.get(p).toString();
                    if(data.startsWith("=")){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        data=data.replaceAll("\"", "");
                        data=data.replaceAll("=", "");
                        cell.setCellValue(data);
                    }else if(data.startsWith("\"")){
                        data=data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(data);
                    }else{
                        data=data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(data);
                    }
                    //*/
                    // cell.setCellValue(ardata.get(p).toString());
                }
                System.out.println();
            }
            FileOutputStream fileOut = new FileOutputStream("D:\\"+todayDate+"\\small-countries-xls\\"+csvFileName+".xls");
            hwb.write(fileOut);
            fileOut.close();
            System.out.println("Your excel file has been generated");
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } //main method ends
    
		
	}
    public static void main(String args[]) throws IOException
    {
    	
    	readNoOfSheet();
		for(File f: allexcelFiles){
			
			System.out.println("excel sheetsName================"+f.getName());
			String fileName = f.getName();
			fileName = fileName.split("\\.")[0];
		
			convertingCSVtoEXCEL(fileName);
			
		}
    	
    	
    }
    
public static int readNoOfSheet() {
		
		int noOfExcelSheets = new File("D:\\small-countries").listFiles().length;
		System.out.println("==========================*************************+++++++++++++++++++++++=="+noOfExcelSheets);
		allexcelFiles = new File("D:\\"+todayDate+"\\small-countries").listFiles();
		
		return noOfExcelSheets;
	}
}