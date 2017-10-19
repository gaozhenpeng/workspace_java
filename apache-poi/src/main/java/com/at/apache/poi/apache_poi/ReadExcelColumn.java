package com.at.apache.poi.apache_poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadExcelColumn {
	private static final Logger logger = LoggerFactory.getLogger(ReadExcelColumn.class);
	private static final String SPOT_COLUMN_NAME = "memberid";

	public static void main(String[] args) {
		if(args == null || args.length != 1){
			logger.warn("Usage:\n    ReadExcelColumn </path/to/excel_file>");
			return;
		}
		
		File excelFile = new File(args[0]);
		if(!excelFile.exists()){
			logger.warn("File does not exists! '{}'", excelFile.getAbsolutePath());
			return;
		}
		String fileName = excelFile.getName();
		if(!(fileName.endsWith("xls") || fileName.endsWith("xlsx"))){
			logger.warn("Filename should end with xls, xlsx. '{}' ", fileName);
			return;
		}
		
		
		// workbook, the whole excel file
		Workbook workbook = null;
		try {
			InputStream is = new FileInputStream(excelFile);
			if (fileName.endsWith("xls")) {
				workbook = new HSSFWorkbook(is);
			} else if (fileName.endsWith("xlsx")) {
				workbook = new XSSFWorkbook(is);
			}
			
		} catch (IOException e) {
			logger.info(e.getMessage());
			throw new RuntimeException(e);
		}
		
		// open first sheet
		Sheet sheet = workbook.getSheetAt(0);
		if(sheet == null){
			logger.warn("No sheet exists in the file.");
			return;
		}
		
		// first row num
		int firstRowNum = sheet.getFirstRowNum();
		
		// read header
		Row header = sheet.getRow(firstRowNum);
		// first cell num
		int firstCellNum = header.getFirstCellNum();
		// last cell num +1
		int lastCellNum_plus1 = header.getLastCellNum();
		
		// member id cell num
		int memberIdCellNum = -1;
		
		// iterate header to get specified column, 'memberid'
		for(int i = firstCellNum ; i < lastCellNum_plus1 ; i++){
			Cell cell = header.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			// jump over null/blank cells
			if(CellType.BLANK.equals(cell.getCellTypeEnum())){
				continue;
			}
			// avoid the case of throwing exception for the cell types other than String
			cell.setCellType(CellType.STRING);
			if(SPOT_COLUMN_NAME.equalsIgnoreCase(cell.getStringCellValue())){
				memberIdCellNum = cell.getColumnIndex();
				break;
			}
		}
		
		if(memberIdCellNum == -1){
			logger.warn("No column named '{}' (ignore case) exists in the header row.", SPOT_COLUMN_NAME); 
			return;
		}
		
		
		
		// result
		List<String> memberids = new ArrayList<String>();
		// last row num
		int lastRowNum = sheet.getLastRowNum();
		for(int j = firstRowNum + 1 ; j <= lastRowNum ; j++){
			Row row = sheet.getRow(j);
			Cell rowcell = row.getCell(memberIdCellNum, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			// jump over null/blank cells
			if(CellType.BLANK.equals(rowcell.getCellTypeEnum())){
				continue;
			}
			// avoid 1 reads 1.0
			rowcell.setCellType(CellType.STRING);
			String memberId = rowcell.getStringCellValue();
			if(!memberId.matches("[0-9]+")){ // only integer format is expected
				logger.warn("Cell format in row {} is not integer.", j);
				continue;
			}
			memberids.add(memberId);
		}
		
		logger.info("Final Result: " + memberids.toString());
		
		
	}
}
