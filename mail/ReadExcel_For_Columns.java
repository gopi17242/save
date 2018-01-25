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
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.examples.CreateUserDefinedDataFormats;

public class ReadExcel_For_Columns{
	/*
	 * To change this template, choose Tools | Templates
	 * and open the template in the editor.
	 */
	
	static List<String> mailidList = new ArrayList<String>();
	static int sentMailCount = 0;
	static int fromMailCount = 0;
	
	static String fromMailId = null;
	static int rn = 0;
	static int rowNum = 0;
	static List<String[]> rowListData = new ArrayList<String[]>();
	static String todayDate ="";
	static File[] allexcelFiles;
	static File[] allCsvFiles;
	public static void main(String[] args) {
		try {
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.now();
			System.out.println(dtf.format(localDate)); //2016/11/16
			todayDate = dtf.format(localDate);
			
			createFolderWithTodayDate(todayDate);
			
			readNoOfCSVSheet();
			for(File f: allCsvFiles){
				
				System.out.println("csv sheetsName================"+f.getName());
				String fileName = f.getName();
				fileName = fileName.split("\\.")[0];
			
				convertingCSVtoEXCEL(fileName);
				
			}

		//	int ns = readNoOfSheet();
			
			readNoOfXLSSheet();
			for(File f: allexcelFiles){
				String fileName = f.getName();
				fileName = fileName.split("\\.")[0];
				System.out.println("excel sheetsName================"+f.getName());
				
				XSSFSheet sheet =  getSheet(fileName);
				//Iterate through each rows one by one
				System.out.println("**********************="+sheet.getLastRowNum());
				getRowData(sheet);
				
				
			}
			
			writeExcel(rowListData,rowNum);
			/*if(rowListData!=null && rowListData.size()>0) {
				for (int i = 0; i < rowListData.size(); i++) {
					
					
				}
			}*/
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void readAndWritesSheets() {
		// TODO Auto-generated method stub
		
	}

	private static XSSFSheet getSheet(String fileName) throws FileNotFoundException, IOException {

		//Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(readExcel(fileName));

		//Get first/desired sheet from the workbook
		XSSFSheet sheet = workbook.getSheetAt(0);

		return sheet;
	}

	private static void getRowData(XSSFSheet sheet) throws MessagingException, IOException {

		Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
		
		///
		
		int noOfColumns = sheet.getRow(0).getPhysicalNumberOfCells();
		
		System.out.println("noOfColumns======88888888888888====*****************************************====="+noOfColumns);
		
		///

		while (rowIterator.hasNext())
		{
			Row row = (Row) rowIterator.next();

			//For each row, iterate through all the columns
			Iterator<Cell> cellIterator = ((org.apache.poi.ss.usermodel.Row) row).cellIterator();
			String[] rowData = new String[row.getPhysicalNumberOfCells()];
			int i=0;
			
			while (cellIterator.hasNext()) 
			{
				Cell cell = cellIterator.next();
				int colIndex = cell.getColumnIndex();
				
				
				if(colIndex == 1 || colIndex == 10 || colIndex == 17) {
				//Check the cell type and format accordingly
				switch (cell.getCellType()) 
				{
				case Cell.CELL_TYPE_NUMERIC:
					//        System.out.print(cell.getNumericCellValue() + "\t");
					rowData[i] = cell.getNumericCellValue()+"";
					break;
				case Cell.CELL_TYPE_STRING:
					//        System.out.print(cell.getStringCellValue() + "\t");
					rowData[i] = cell.getStringCellValue();
					break;
				}
				System.out.println("colIndex==============================================****="+colIndex);
				}
				i++;
			}
			rn++;

			startDataProcess(rowData[17], rowData);

		}

	}

	private static void startDataProcess(String mailId,String[] rowData) throws MessagingException, IOException {

		KeyWords keyWords = new KeyWords();

		for(String listItem : keyWords.keyWords()){
		System.out.println("=========rowData.length================="+rowData.length);
			if(mailId!=null && mailId.contains(listItem)){
				// do something.
				if(!avoidDuplicate(mailId) && !isStartWithDigit(rowData[1])){
					processRowData(rowData, rn);
				}
			}
		}

	}
	
	@SuppressWarnings("unused")
	public static void processRowData(String[] rowData, int rn) throws MessagingException, IOException{
		String name = rowData[1];		
		String email = rowData[4];
		///
		
		String[] rowData1 = new String[3];
		
		rowData1[0] = rowData[1];
		rowData1[1] = rowData[10];
		rowData1[2] = rowData[17];
		
		
		///
		rowListData.add(rowData1);
	}

	public static boolean isStartWithDigit(String doaminName) {
		boolean flag = false;
		if ( Character.isDigit(doaminName.charAt(0)) )
		{
			
		    System.out.println("String begins with a digit");
		    flag = true;
		}
		
		return flag;
	}
	

public static void writeExcel(List<String[]> rowListData, int rowNum) throws IOException{
		String excelname = "DataLeads";
		String excelFileName = "D:/"+excelname+".xlsx";//name of excel file
		
		

		String sheetName = "Sheet1";//name of sheet
		int rowsCount = 0;
		int fileCount = 0;
		//iterating r number of rows
		System.out.println("rowListData.size()======"+rowListData.size());
		XSSFWorkbook wb =  new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName) ;	
		for (int r=0;r < rowListData.size(); r++ )
		{
			
				
			
			if(rowsCount == 10) {
				fileCount++;
				FileOutputStream fileOut = new FileOutputStream("D:/"+todayDate+"/"+excelname+fileCount+".xlsx");
						
				//write this workbook to an Outputstream.
				wb.write(fileOut);
				fileOut.flush();
				fileOut.close();
				wb = new XSSFWorkbook();
				sheet = wb.createSheet(sheetName) ;	
				
				
				System.out.println("rowsCount======"+rowsCount);
				rowsCount = 0;
				rowNum = 0;
			}

			XSSFRow row = sheet.createRow(rowNum);
			String[]  rowData = rowListData.get(r);
			
			//iterating c number of columns
			for (int c=0;c < rowData.length; c++ )
			{
				XSSFCell cell = row.createCell(c);
				if(rowData[c]!=null)
				cell.setCellValue(rowData[c].toString());
			
			}
			System.out.println(r);
			rowNum++;
			rowsCount++;
			
					}
		
		System.out.println("======close==========");

	
		
	}


	public static FileInputStream readExcelOneByOne(String excelFileName) throws FileNotFoundException{
	FileInputStream file;
	file = new FileInputStream(new File("D:\\"+todayDate+"\\small-countries\\"+excelFileName+".xlsx")); // to generate excels each with 25 valid mailid's

	return file;
}

	public static FileInputStream readExcel() throws FileNotFoundException{
		FileInputStream file;
		file = new FileInputStream(new File("C:\\Users\\gopinadh\\Desktop\\Leads-"+todayDate+".xlsx")); // to generate excels each with 25 valid mailid's
	//	file = new FileInputStream(new File("D:\\"+todayDate+"\\"+leadsFileName+".xlsx")); // to send mails 

		return file;
	}

	public static FileInputStream readExcel(String excelFileName) throws FileNotFoundException{
		FileInputStream file;
		file = new FileInputStream(new File("D:\\"+todayDate+"\\small-countries-xls\\"+excelFileName+".xlsx")); // to generate excels each with 25 valid mailid's
	//	file = new FileInputStream(new File("D:\\"+todayDate+"\\"+leadsFileName+".xlsx")); // to send mails 

		return file;
	}
	public static int readNoOfCSVSheet() {
		
		int noOfExcelSheets = new File("D:\\"+todayDate+"\\small-countries-csv").listFiles().length;
		System.out.println("==========================*************************+++++++++++++++++++++++=="+noOfExcelSheets);
		allCsvFiles = new File("D:\\"+todayDate+"\\small-countries-csv").listFiles();
		
		return noOfExcelSheets;
	}
public static int readNoOfXLSSheet() {
		
		int noOfExcelSheets = new File("D:\\"+todayDate+"\\small-countries-xls").listFiles().length;
		System.out.println("==========================*************************+++++++++++++++++++++++=="+noOfExcelSheets);
		
		allexcelFiles = new File("D:\\"+todayDate+"\\small-countries-xls").listFiles();
		
		return noOfExcelSheets;
	}
	public static boolean avoidDuplicate(String mailId){

		boolean isHasMailId = mailidList.contains(mailId);
		if(!isHasMailId){
			mailidList.add(mailId);
		}
		return isHasMailId;
	}	
	
	public static void createFolderWithTodayDate(String date){
		
		new File("D:\\"+date).mkdir();
		new File("D:\\"+date+"\\small-countries-xls").mkdir();
		
		
		
	}
	
public static void convertingCSVtoEXCEL(String csvFileName) throws IOException {
		

    	
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		System.out.println(dtf.format(localDate)); //2016/11/16
		todayDate = dtf.format(localDate);
        ArrayList arList=null;
        ArrayList al=null;
        String fName = "D:\\"+todayDate+"\\small-countries-csv\\"+csvFileName+".csv";
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
        	XSSFWorkbook hwb = new XSSFWorkbook();
        	XSSFSheet sheet = hwb.createSheet("sheet1");
            for(int k=0;k<arList.size();k++)
            {
                ArrayList ardata = (ArrayList)arList.get(k);
                XSSFRow row = sheet.createRow((short) 0+k);
                for(int p=0;p<ardata.size();p++)
                {
                	XSSFCell cell = row.createCell((short) p);
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
            FileOutputStream fileOut = new FileOutputStream("D:\\"+todayDate+"\\small-countries-xls\\"+csvFileName+".xlsx");
            hwb.write(fileOut);
            fileOut.close();
            System.out.println("Your excel file has been generated");
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } //main method ends
    
		
	}
	
	
}

/*
needed jar

poi-scratchpad-3.7-20101029
poi-3.2-FINAL-20081019
poi-3.7-20101029
poi-examples-3.7-20101029
poi-ooxml-3.7-20101029
poi-ooxml-schemas-3.7-20101029
xmlbeans-2.3.0
dom4j-1.6.1
 */