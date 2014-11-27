package com.walmart.releaseautomation.weekly.dashboard.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.walmart.logistics.weekly.dashboard.util.DashboardUtility;
import com.walmart.releaseautomation.weekly.dashboard.constants.DashboardConstants;
import com.walmart.releaseautomation.weekly.dashboard.intf.Rules;
import com.walmart.releaseautomation.weekly.dashboard.model.Filter;
import com.walmart.releaseautomation.weekly.dashboard.model.Rule;
import com.walmart.releaseautomation.weekly.dashboard.model.Update;

public class RulesImpl implements Rules {
	private int filterColIndex = -1;
	private int updateColIndex = -1;
	private int rowCountColIndex = -1;
	private boolean newUpdCol;
	private Rule rule;
	private List<Integer> toEvalColIndexes;
	private boolean breaker;
	private boolean wbChanged;

	/**
	 * Filter-DefectsReport.xls|1|Defect Root
	 * Cause|Blanks~Update-DefectsReport|1|RCA Completed|N
	 * 
	 * @throws IOException
	 */
	public void filterAndUpdate(Rule rule) throws IOException {
		setRule(rule);
		Filter filter = rule.getFilter();
		Update update = rule.getUpdate();
		Date createdDate = null;
		Map<Date, List<Integer>> storeCountMap = new HashMap<Date, List<Integer>>();
		HSSFWorkbook filterWorkbook = (HSSFWorkbook) DashboardUtility
				.getWorkBook(DashboardConstants.wbURLMap.get(filter
						.getFileToFilter().split(".x")[0]));
		Sheet filterSheet = DashboardUtility.getSheet(
				filter.getSheetToFilter(), filterWorkbook);
		for (Row row : filterSheet) {
			if (getFilterColIndex() == -1 && getUpdateColIndex() == -1) {
				Cell cell;
				String tempCell = update.getColToUpdate().replace("^", "");
				for (Iterator<Cell> iterator = row.iterator(); iterator
						.hasNext();) {
					cell = iterator.next();
					if (getFilterColIndex() == -1
							&& cell.getStringCellValue().equals(
									filter.getColToFilter())) {
						setFilterColIndex(cell.getColumnIndex());
					}
					if (getUpdateColIndex() == -1
							&& cell.getStringCellValue().equals(tempCell)) {
						setUpdateColIndex(cell.getColumnIndex());
					} else if (getUpdateColIndex() == -1
							&& cell.getColumnIndex() + 1 == row
									.getPhysicalNumberOfCells()
							&& update.getColToUpdate().contains("^")) {
						Cell newColumnCell = row.createCell(row
								.getPhysicalNumberOfCells());
						newColumnCell.setCellValue(update.getColToUpdate()
								.replace("^", ""));
						setUpdateColIndex(newColumnCell.getColumnIndex());
						setNewUpdCol(Boolean.TRUE);
					}
					if (getRowCountColIndex() == -1
							&& rule.getCount() != null
							&& cell.getStringCellValue().equals(
									rule.getCount().getCountFromCol())) {
						setRowCountColIndex(cell.getColumnIndex());
					}
				}
			} else if (row.getCell(getFilterColIndex()) != null) {
				if (filter.getValuesToFilter().size() == 1) {
					String valueToFilter = filter.getValuesToFilter().get(0);
					int filterColCellType = row.getCell(getFilterColIndex())
							.getCellType();
					if (valueToFilter
							.equalsIgnoreCase(DashboardConstants.BLANK_CELL_STR)
							&& filterColCellType == Cell.CELL_TYPE_BLANK) {
						setCellValue(row);
					} else if (valueToFilter
							.equalsIgnoreCase(DashboardConstants.NOT_OF_BLANK_CELL_STR)
							&& filterColCellType != Cell.CELL_TYPE_BLANK) {
						setCellValue(row);
					} else if (filter.isFilterColADate()) {
						if (createdDate == null) {
							Calendar calendar = Calendar.getInstance();
							calendar.add(Calendar.DATE, -Integer
									.parseInt(valueToFilter.split("-")[1]));
							createdDate = calendar.getTime();
						}
						if (row.getCell(getFilterColIndex()).getDateCellValue()
								.after(createdDate)) {
							setCellValue(row);
							if (rule.getCount().isStoreCountInMap()
									&& getRowCountColIndex() != -1) {
								getPriority(row, storeCountMap);
							}
						}
					}
				} else {
					if (filter.getValuesToFilter().contains(
							row.getCell(getFilterColIndex())
									.getStringCellValue())) {
						setCellValue(row);
					}
				}

			}
		}
		//fsadfakfj;lk
		if (rule.getCount() != null && !storeCountMap.isEmpty()) {
			Sheet countOnSheet = DashboardUtility.getSheet(rule.getCount()
					.getCountOnSheet(), filterWorkbook);
			try {
				countOnSheet.createFreezePane(12, 3); 
				writeBack(filter, filterWorkbook);
				filterWorkbook = (HSSFWorkbook) DashboardUtility
						.getWorkBook(DashboardConstants.wbURLMap.get(filter
								.getFileToFilter().split(".x")[0]));
				countOnSheet = DashboardUtility.getSheet(rule.getCount()
						.getCountOnSheet(), filterWorkbook);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("previously didn't have a freeze pane.");
			}
			Row row = null;
			Cell dateCell = null;
			Cell highPriority = null;
			Cell mediumPriority = null;
			Cell lowPriority = null;
			int lastRow = countOnSheet.getLastRowNum();
			for (Date createDate : storeCountMap.keySet()) {
				row = countOnSheet.createRow(lastRow++);
				dateCell = row.createCell(1);
				dateCell.setCellValue(createDate);
				highPriority = row.createCell(2);
				highPriority.setCellValue(storeCountMap.get(createDate).get(0));
				mediumPriority = row.createCell(3);
				mediumPriority.setCellValue(storeCountMap.get(createDate)
						.get(1));
				lowPriority = row.createCell(4);
				lowPriority.setCellValue(storeCountMap.get(createDate).get(2));
			}
		}

		setFilterColIndex(-1);
		setUpdateColIndex(-1);
		if (isWbChanged()) {
			writeBack(filter, filterWorkbook);
		}
	}

	private void getPriority(Row row, Map<Date, List<Integer>> storeCountMap) {
		String high = "high";
		String medium = "medium";
		String low = "low";
		LinkedList<Integer> priorities = null;
		if (storeCountMap.containsKey(row.getCell(getFilterColIndex())
				.getDateCellValue())) {
			priorities = (LinkedList<Integer>) storeCountMap.get(row.getCell(
					getFilterColIndex()).getDateCellValue());
			if (row.getCell(getRowCountColIndex()).getStringCellValue()
					.toLowerCase().contains(high)) {
				priorities.set(0, priorities.get(0) + 1);
			} else if (row.getCell(getRowCountColIndex()).getStringCellValue()
					.contains(medium)) {
				priorities.set(1, priorities.get(1) + 1);
			} else if (row.getCell(getRowCountColIndex()).getStringCellValue()
					.contains(low)) {
				priorities.set(2, priorities.get(2) + 1);
			}
		} else {
			priorities = new LinkedList<Integer>();
			priorities.add(0);
			priorities.add(0);
			priorities.add(0);
			if (row.getCell(getRowCountColIndex()).getStringCellValue()
					.toLowerCase().contains(high)) {
				priorities.set(0, priorities.get(0) + 1);
			} else if (row.getCell(getRowCountColIndex()).getStringCellValue()
					.toLowerCase().contains(medium)) {
				priorities.set(1, priorities.get(1) + 1);
			} else if (row.getCell(getRowCountColIndex()).getStringCellValue()
					.toLowerCase().contains(low)) {
				priorities.set(2, priorities.get(2) + 1);
			}
		}
		setWbChanged(Boolean.TRUE);
		storeCountMap.put(row.getCell(getFilterColIndex()).getDateCellValue(),
				priorities);
	}

	/**
	 * @param filter
	 * @param filterWorkbook
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeBack(Filter filter, HSSFWorkbook filterWorkbook)
			throws FileNotFoundException, IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(
				DashboardConstants.wbURLMap.get(filter.getFileToFilter().split(
						".x")[0]));
		filterWorkbook.write(fileOutputStream);
		fileOutputStream.close();
	}

	/**
	 * @param row
	 */
	private void setCellValue(Row row) {
		setWbChanged(Boolean.TRUE);
		if (isNewUpdCol()) {
			row.createCell(getUpdateColIndex());
		}
		if (rule.getUpdate().getValueToUpdate().equals("*")) {
			row.getCell(getUpdateColIndex()).setCellValue(
					row.getCell(getFilterColIndex()).getStringCellValue());
		} else {
			row.getCell(getUpdateColIndex()).setCellValue(
					rule.getUpdate().getValueToUpdate());
		}
	}

	public void copyData(Rule rule) throws IOException, BiffException {
		Workbook workbook = null;
		CellStyle cellStyle = null;
		CreationHelper creationHelper = null;
		Calendar calendar = null;
		SimpleDateFormat simpleDateFormat = null;
		List<Integer> copyColumnsIndexes = new LinkedList<Integer>();
		HSSFWorkbook copyFrmWB = (HSSFWorkbook) DashboardUtility
				.getWorkBook(DashboardConstants.wbURLMap.get(rule.getCopyData()
						.getCopyFromFile().split(".x")[0]));
		Sheet sheet = DashboardUtility.getSheet(rule.getCopyData()
				.getCopyFromSheet(), copyFrmWB);

		for (Row row : sheet) {
			for (Cell cell : row) {
				for (String copyCol : rule.getCopyData().getCopyFromColumns()) {
					if (cell.getStringCellValue().equalsIgnoreCase(copyCol)) {
						copyColumnsIndexes.add(cell.getColumnIndex());
						continue;
					} else if (copyCol.equals("*")) {
						copyColumnsIndexes.add(cell.getColumnIndex());
						continue;
					}
				}
			}
			break;
		}
		workbook = Workbook.getWorkbook(new File(DashboardConstants.wbURLMap
				.get(rule.getCopyData().getCopyFromFile().split(".x")[0])));
		jxl.Sheet jxlSheet = workbook.getSheet(sheet.getSheetName());
		List<jxl.Cell[]> copiedCells = new LinkedList<jxl.Cell[]>();
		for (Integer i : copyColumnsIndexes) {
			copiedCells.add((jxl.Cell[]) jxlSheet.getColumn(i));
		}
		List<jxl.Cell[]> rowWiseCells = new ArrayList<jxl.Cell[]>();
		jxl.Cell[] cellArray = new jxl.Cell[copiedCells.size()];
		int colIndex = 0;
		for (int i = 0; i < copiedCells.get(0).length; i++) {
			while (colIndex < cellArray.length) {
				cellArray[colIndex] = copiedCells.get(colIndex)[i];
				colIndex++;
			}
			colIndex = 0;
			rowWiseCells.add(cellArray);
			cellArray = new jxl.Cell[copiedCells.size()];
		}

		HSSFWorkbook pasteToWB = (HSSFWorkbook) DashboardUtility
				.getWorkBook(DashboardConstants.wbURLMap.get(rule.getCopyData()
						.getPasteToFile().split(".x")[0]));
		Sheet pasteToSheet = DashboardUtility.getSheet(rule.getCopyData()
				.getPasteToSheet().replace("^", ""), pasteToWB);
		if (pasteToSheet == null
				&& rule.getCopyData().getPasteToSheet().contains("^")) {
			pasteToSheet = pasteToWB.createSheet(rule.getCopyData()
					.getPasteToSheet().replace("^", ""));
		}
		colIndex = 0;
		if (pasteToSheet.getPhysicalNumberOfRows() != 0) {
			for (Row row : pasteToSheet) {
				while (colIndex < rowWiseCells.get(0).length) {
					Cell cell = row.getCell(colIndex);
					jxl.Cell toBePasteCell = rowWiseCells.get(row.getRowNum())[colIndex];
					cell.setCellType(DashboardConstants.CELL_TYPE.valueOf(
							toBePasteCell.getType().toString().toUpperCase())
							.getCellType());
					cell.setCellValue(toBePasteCell.getContents());
					colIndex++;
				}
				colIndex = 0;
			}
		} else {
			colIndex = 0;
			cellStyle = pasteToWB.createCellStyle();
			creationHelper = pasteToWB.getCreationHelper();
			for (int i = 0; i < rowWiseCells.size(); i++) {
				Row row = pasteToSheet.createRow(i);
				while (colIndex < rowWiseCells.get(0).length) {
					row.createCell(
							rowWiseCells.get(i)[colIndex].getColumn(),
							DashboardConstants.CELL_TYPE.valueOf(
									rowWiseCells.get(i)[colIndex].getType()
											.toString().toUpperCase())
									.getCellType());
					if (rowWiseCells.get(i)[colIndex].getType().toString()
							.toUpperCase().equalsIgnoreCase("DATE")) {
						try {
							cellStyle
									.setDataFormat(creationHelper
											.createDataFormat().getFormat(
													"dd/mm/yyyy"));
							simpleDateFormat = new SimpleDateFormat("dd/mm/yy");
							Date date = simpleDateFormat.parse(rowWiseCells
									.get(i)[colIndex].getContents());
							calendar = Calendar.getInstance();
							calendar.setTime(date);
							calendar.set(Calendar.HOUR, 0);
							calendar.set(Calendar.MINUTE, 0);
							calendar.set(Calendar.SECOND, 0);
							row.getCell(
									rowWiseCells.get(i)[colIndex].getColumn())
									.setCellValue(calendar.getTime());
							row.getCell(
									rowWiseCells.get(i)[colIndex].getColumn())
									.setCellStyle(cellStyle);
							System.out.println(row
									.getCell(
											rowWiseCells.get(i)[colIndex]
													.getColumn())
									.getCellStyle().getDataFormat()
									+ ":"
									+ row.getCell(
											rowWiseCells.get(i)[colIndex]
													.getColumn())
											.getCellStyle()
											.getDataFormatString());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						row.getCell(rowWiseCells.get(i)[colIndex].getColumn())
								.setCellValue(
										rowWiseCells.get(i)[colIndex]
												.getContents());
					}
					colIndex++;
				}
				colIndex = 0;
			}
		}

		FileOutputStream fileOutputStream = new FileOutputStream(new File(
				DashboardConstants.wbURLMap.get(rule.getCopyData()
						.getPasteToFile().split(".x")[0])));
		pasteToWB.write(fileOutputStream);
		fileOutputStream.close();
	}

	public void formatColumn(Rule rule) {
		// TODO Auto-generated method stub

	}

	public void executeMacro(Rule rule) throws IOException,
			InterruptedException {
		executeProcess(rule.getExecuteMacro().getScriptName());
	}

	public void evaluateFormula(Rule rule) throws IOException {
		HSSFWorkbook evalFrmWB = (HSSFWorkbook) DashboardUtility
				.getWorkBook(DashboardConstants.wbURLMap.get(rule
						.getEvaluateFormula().getFromFile().split(".x")[0]));
		Sheet sheet = DashboardUtility.getSheet(rule.getEvaluateFormula()
				.getFromSheet(), evalFrmWB);
		FormulaEvaluator evaluator = evalFrmWB.getCreationHelper()
				.createFormulaEvaluator();
		for (Row row : sheet) {
			if (isBreaker()) {
				break;
			} else if (row.isFormatted() && getToEvalColIndexes().isEmpty()) {
				for (int eval = 0; eval < rule.getEvaluateFormula().getCell().length; eval++) {
					Cell cell = null;
					for (Iterator<Cell> iterator = row.iterator(); iterator
							.hasNext();) {
						cell = iterator.next();
						if (cell.getStringCellValue().equals(
								rule.getEvaluateFormula().getCell()[eval]
										.getHeader())) {
							getToEvalColIndexes().add(cell.getColumnIndex());
						}
					}
				}
			} else {
				System.out.println(row.getRowNum());
				for (int eachEvalColIndex = 0; eachEvalColIndex < getToEvalColIndexes()
						.size(); eachEvalColIndex++) {
					Cell cell = row.getCell(getToEvalColIndexes().get(
							eachEvalColIndex));
					if (cell != null) {
						if (rule.getEvaluateFormula().getCell()[eachEvalColIndex]
								.getCellType().equalsIgnoreCase("Date")) {
							cell.setCellValue(new Date());
						}
						cell.setCellFormula(rule.getEvaluateFormula().getCell()[eachEvalColIndex]
								.getFormula());
						evaluator.evaluate(cell);
					} else {
						setBreaker(true);
					}
				}
			}
		}
		FileOutputStream outputStream = new FileOutputStream(
				DashboardConstants.wbURLMap.get(rule.getEvaluateFormula()
						.getFromFile().split(".x")[0]));
		evalFrmWB.write(outputStream);
		outputStream.close();
	}

	public void deleteData(Rule rule) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the filterColIndex
	 */
	public int getFilterColIndex() {
		return filterColIndex;
	}

	/**
	 * @param filterColIndex
	 *            the filterColIndex to set
	 */
	public void setFilterColIndex(int filterColIndex) {
		this.filterColIndex = filterColIndex;
	}

	/**
	 * @return the updateColIndex
	 */
	public int getUpdateColIndex() {
		return updateColIndex;
	}

	/**
	 * @param updateColIndex
	 *            the updateColIndex to set
	 */
	public void setUpdateColIndex(int updateColIndex) {
		this.updateColIndex = updateColIndex;
	}

	/**
	 * @return the newUpdCol
	 */
	public boolean isNewUpdCol() {
		return newUpdCol;
	}

	/**
	 * @param newUpdCol
	 *            the newUpdCol to set
	 */
	public void setNewUpdCol(boolean newUpdCol) {
		this.newUpdCol = newUpdCol;
	}

	/**
	 * @return the rule
	 */
	public Rule getRule() {
		return rule;
	}

	/**
	 * @param rule
	 *            the rule to set
	 */
	public void setRule(Rule rule) {
		this.rule = rule;
	}

	private void executeProcess(String scriptName) throws IOException {
		String fileWithRscDir = "rsc/vbs/" + scriptName;
		String fullScriptPath = this.getClass().getClassLoader()
				.getResource(fileWithRscDir).getFile();
		fullScriptPath = URLDecoder.decode(fullScriptPath, "UTF-8");
		if (fullScriptPath.charAt(0) == '/') {
			fullScriptPath = fullScriptPath.substring(1,
					fullScriptPath.length());
		}
		Process p = null;
		int processOutputCode = 0;
		try {
			p = Runtime.getRuntime().exec(
					"wscript" + "\"" + fullScriptPath + "\"");
			processOutputCode = p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the toEvalColIndexes
	 */
	public List<Integer> getToEvalColIndexes() {
		if (toEvalColIndexes == null) {
			toEvalColIndexes = new ArrayList<Integer>();
		}
		return toEvalColIndexes;
	}

	/**
	 * @param toEvalColIndexes
	 *            the toEvalColIndexes to set
	 */
	public void setToEvalColIndexes(List<Integer> toEvalColIndexes) {
		this.toEvalColIndexes = toEvalColIndexes;
	}

	/**
	 * @return the breaker
	 */
	public boolean isBreaker() {
		return breaker;
	}

	/**
	 * @param breaker
	 *            the breaker to set
	 */
	public void setBreaker(boolean breaker) {
		this.breaker = breaker;
	}

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		System.out.println(calendar.getTime());

	}

	/**
	 * @return the rowCountColIndex
	 */
	public int getRowCountColIndex() {
		return rowCountColIndex;
	}

	/**
	 * @param rowCountColIndex
	 *            the rowCountColIndex to set
	 */
	public void setRowCountColIndex(int rowCountColIndex) {
		this.rowCountColIndex = rowCountColIndex;
	}

	/**
	 * @return the wbChanged
	 */
	public boolean isWbChanged() {
		return wbChanged;
	}

	/**
	 * @param wbChanged
	 *            the wbChanged to set
	 */
	public void setWbChanged(boolean wbChanged) {
		this.wbChanged = wbChanged;
	}
}
