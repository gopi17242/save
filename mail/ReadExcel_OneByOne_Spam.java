package com.mail;

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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel_OneByOne_Spam{
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
	static String fromMailIdUsername = "";
	static String fromMailIdPassword = "";
	static String leadsFileName = "";
	static String todayDate ="";
	static FileInputStream file = null;
	public static void main(String[] args) {
	//	public static void main_test(int fromMailNum) {
		try {
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.now();
	//		System.out.println(dtf.format(localDate)); //2016/11/16
			todayDate = dtf.format(localDate);
			rowListData.clear();
			/*fromMailIdUsername = "techfloppy"+fromMailNum+"@gmail.com";
			fromMailIdPassword = "techfloppy@0";//"+args[1];
			leadsFileName = "DataLeads"+fromMailNum;*/
			
			fromMailIdUsername = "techfloppy"+args[0]+"@gmail.com";
			fromMailIdPassword = "techfloppy@0";//"+args[1];
			leadsFileName = "DataLeads1";

			XSSFSheet sheet =  getSheet();
			//Iterate through each rows one by one
			System.out.println("**********************="+sheet.getLastRowNum());
			getRowData(sheet);
		//	writeExcel(rowListData,rowNum);
			file.close();
			if(rowListData!=null && rowListData.size()>0) {
				for (int i = 0; i < rowListData.size(); i++) {
					
					sendMail(rowListData.get(i)[1],rowListData.get(i)[2],rowListData.get(i)[4]);
					
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static XSSFSheet getSheet() throws FileNotFoundException, IOException {

		//Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(readExcel());

		//Get first/desired sheet from the workbook
		XSSFSheet sheet = workbook.getSheetAt(0);

		return sheet;
	}

	private static void getRowData(XSSFSheet sheet) throws MessagingException, IOException {

		Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();

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
				i++;
			}
			rn++;

			startDataProcess(rowData[4], rowData);

		//	System.out.println("");
		}

	}

	private static void startDataProcess(String mailId,String[] rowData) throws MessagingException, IOException {

		KeyWords keyWords = new KeyWords();

	//	for(String listItem : keyWords.keyWords()){
		System.out.println("=========rowData.length================="+rowData.length);
		for(int r = 0 ; r<rowData.length; r++) {

		//	if(mailId!=null && mailId.contains(listItem)){
				// do something.
				if(!avoidDuplicate(mailId) && !isStartWithDigit(mailId)){
					processRowData(rowData, rn);
				}
	//		}
		}

	}

	public static void processRowData(String[] rowData, int rn) throws MessagingException, IOException{
	//	rowNum++;
		String name = rowData[1];
		String email = rowData[4];
		rowListData.add(rowData);
	//	sendMail(name,email);
	}

	public static boolean isStartWithDigit(String mailId) {
		boolean flag = false;
		if ( Character.isDigit(mailId.charAt(0)) )
		{
		    System.out.println("String begins with a digit");
		    flag = true;
		}
		
		return flag;
	}
	@SuppressWarnings("static-access")
	public static void sendMail(String name,String domainName,String email) throws MessagingException{
		String toMail = email;
		ActivationMail actMail = new ActivationMail();

		sentMailCount++;
		MailIdDetails mailIdDetails = new MailIdDetails();
		
		System.out.println("sentMailCount===="+sentMailCount);
		if(sentMailCount >= 10){

			sentMailCount = 0;
			fromMailCount++;
		}

		actMail.sendMail(name,domainName,toMail,fromMailIdUsername,fromMailIdPassword);
		System.out.println("************mails sending finished**************");
	//	String from =  fromMailDetails.getUsername();
	//	System.out.println(email+" , "+name+" , "+fromMailDetails.getUsername());
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

	public static FileInputStream readExcel() throws FileNotFoundException{
		
	//	file = new FileInputStream(new File("C:\\Users\\gopinadh\\Desktop\\united_kingdom-leads.xlsx")); // to generate excels each with 25 valid mailid's
		file = new FileInputStream(new File("D:\\"+todayDate+"\\"+leadsFileName+".xlsx")); // to send mails 

		return file;
	}

	public static boolean avoidDuplicate(String mailId){

		boolean isHasMailId = mailidList.contains(mailId);
		if(!isHasMailId){
			mailidList.add(mailId);
		}
		return isHasMailId;
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