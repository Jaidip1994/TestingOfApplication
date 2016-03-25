package com.sap.solman.utility;
/**
 * Excel utility to read excel file and to write into the excel file 
 *@author Jaidip Ghosh
 *@version 1.0
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	private static Sheet excelWSheet;

	private static Workbook excelWBook;

	/**
	 * This method is to set the File path and to open the Excel file, Pass
	 * Excel Path and Sheetname as Arguments to this method
	 *
	 * @param path
	 *            path to the sheet
	 * @param sheetName
	 *            Sheet of the spreadsheet
	 * @throws java.lang.Exception
	 *             if sheet does not exist, excel does not exist, error during
	 *             read
	 */
	public static void setExcelFile(String path, String sheetName)
			throws Exception {

		// Open the Excel file

		FileInputStream excelFile = new FileInputStream(path);
		

		// Access the required test data sheet
		
		excelWBook = new XSSFWorkbook(excelFile);
		
		excelWSheet = excelWBook.getSheet(sheetName);

		excelFile.close();
	}

	/**
	 * This method is to read the test data from the Excel cell, in this we are
	 * passing parameters as Row num and Column num
	 *
	 * @param rowNum
	 *            row number
	 * @param colNum
	 *            Column number
	 * @return String
	 * 
	 */

	public static String getCellData(int rowNum, int colNum) {
		Cell cell = null;

		try {

			cell = excelWSheet.getRow(rowNum).getCell(colNum);

			String cellData = cell.getStringCellValue();
			return cellData;

		} catch (Exception e) {
			// trying to read a numeric value as string could lead to an
			// exception, hence read the int, convert to string and return
			try {
				
				Double cellval = cell.getNumericCellValue();
				//int intVal = cellval.intValue();
				String strVal = Double.toString(cellval);
				return strVal;
				
			} catch (Exception e2) {
				return "";
			}

		}

	}

	/**
	 * This method is to write in the Excel cell, Row num and Col num are the
	 * parameters
	 *
	 * @param result
	 *            value to be written
	 * @param rowNum
	 *            row number
	 * @param colNum
	 *            Column Number
	 * @throws java.lang.Exception
	 *             if value cannot be read properly and an error occurs
	 */
	public static void setCellData(String result, int rowNum, int colNum)
			throws Exception {
		Cell cell = null;
		Row row = null;

		row = excelWSheet.getRow(rowNum);

		cell = row.getCell(colNum, Row.RETURN_BLANK_AS_NULL);

		if (cell == null) {

			cell = row.createCell(colNum);
		}
		cell.setCellValue(result);

	}

	/**
	 * This method returns the opened Sheet
	 *
	 * @return XSSFSheet excel sheet
	 */
	public static Sheet getSelectedSheet() {
		return excelWSheet;
	}

	/**
	 * This method writes out the workbook to an output Stream and 
	 * flushes this output stream and forces any buffered output bytes to be written out.
	 * 
	 * @throws java.lang.Exception
	 *             if writing to sheet leads to error or file does not exist
	 */
	public static void saveSheet() throws Exception {

		// Constant variables Test Data path and Test Data file name, uses resources
		// pattern the close method of FileOutputStream is automatically called
		try (FileOutputStream fileOut = new FileOutputStream(
				Constant.PATH_TESTDATA + Constant.FILE_TESTDATA)) {
			excelWBook.write(fileOut);
			fileOut.flush();
		}

	}

}