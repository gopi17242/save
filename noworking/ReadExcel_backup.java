package com.noworking;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import com.mail.FromMailDetails;
import com.mail.KeyWords;
import com.mail.MailIdDetails;

public class ReadExcel_backup {
	/*
	 * To change this template, choose Tools | Templates
	 * and open the template in the editor.
	 */
	static FileInputStream file;
	static List<String> mailidList = new ArrayList<String>();
	static int sentMailCount = 0;
	static int fromMailCount = 0;
	static String fromMailId = null;
	static int rn = 0;
	static int rowNum = 0;
	static int totalNumOfRows = 0;
	static List<String[]> rowListData = new ArrayList<String[]>();


	public static void main(String[] args) {
		try {



			XSSFSheet sheet =  getSheet();
			//Iterate through each rows one by one
			System.out.println("**********************="+sheet.getLastRowNum());
			getRowData(sheet,totalNumOfRows);
			writeExcel(rowListData,rowNum);
			if(rowListData!=null && rowListData.size()>0) {
				for (int i = 0; i < rowListData.size(); i++) {
					
					sendMail(rowListData.get(i)[1],rowListData.get(i)[2],rowListData.get(i)[4]);
					
				}
			}
			file.close();
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

	private static void getRowData(XSSFSheet sheet,int totalNumOfRows) throws MessagingException, IOException {

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

			startDataProcess(rowData[4], rowData,totalNumOfRows);

		//	System.out.println("");
		}

	}

	private static void startDataProcess(String mailId,String[] rowData,int totalNumOfRows) throws MessagingException, IOException {

		KeyWords keyWords = new KeyWords();

		for(String listItem : keyWords.keyWords()){

			if(mailId!=null && mailId.contains(listItem)){
				// do something.
				if(!avoidDuplicate(mailId)){
					processRowData(rowData, rn,totalNumOfRows);
				}
			}
		}

	}

	public static void processRowData(String[] rowData, int rn, int totalNumOfRows) throws MessagingException, IOException{
	//	rowNum++;
		String name = rowData[1];
		String email = rowData[4];
		rowListData.add(rowData);
	//	sendMail(name,email);
	}

	@SuppressWarnings("static-access")
	public static void sendMail(String name,String domainName,String email) throws MessagingException{
		String toMail = email;
		ActivationMail1 actMail = new ActivationMail1();

		sentMailCount++;
		MailIdDetails mailIdDetails = new MailIdDetails();
		FromMailDetails fromMailDetails = new FromMailDetails();
		fromMailDetails = mailIdDetails.fromMailIdDetails().get(fromMailCount);
		
		System.out.println("sentMailCount===="+sentMailCount);
		if(sentMailCount >= 25){			
			sentMailCount = 0;
			fromMailCount++;
		}

		actMail.sendMail(name,domainName,toMail,fromMailDetails.getUsername(),fromMailDetails.getPassword());
		System.out.println("Hi "+name+" mail sent to "+email+" from "+ fromMailDetails.getUsername());
	//	String from =  fromMailDetails.getUsername();
	//	System.out.println(email+" , "+name+" , "+fromMailDetails.getUsername());
	}

public static void writeExcel(List<String[]> rowListData, int rowNum) throws IOException{
		
		String excelFileName = "D:/Test.xlsx";//name of excel file

		String sheetName = "Sheet1";//name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName) ;

		//iterating r number of rows
		for (int r=0;r < rowListData.size(); r++ )
		{
			XSSFRow row = sheet.createRow(rowNum);

			String[]  rowData = rowListData.get(r);
			
			//iterating c number of columns
			for (int c=0;c < rowData.length; c++ )
			{
				XSSFCell cell = row.createCell(c);
	
				cell.setCellValue(rowData[c].toString());
			}
			rowNum++;
		}

	//		System.out.println(row.getCell(0)+" "+row.getCell(1)+" "+row.getCell(2)+" "+row.getCell(3)+" "+row.getCell(4));
		FileOutputStream fileOut = new FileOutputStream(excelFileName);

		//write this workbook to an Outputstream.
	wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
		
	}

	public static FileInputStream readExcel() throws FileNotFoundException{

		file = new FileInputStream(new File("C:\\Users\\gopinadh\\Desktop\\FinalLeads.xlsx"));

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