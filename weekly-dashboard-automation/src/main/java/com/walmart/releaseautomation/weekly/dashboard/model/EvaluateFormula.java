package com.walmart.releaseautomation.weekly.dashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EvaluateFormula {
	private String fromFile;
	private String fromSheet;
	private Cell[] cell;

	/**
	 * @return the fromFile
	 */
	public String getFromFile() {
		return fromFile;
	}

	/**
	 * @param fromFile
	 *            the fromFile to set
	 */
	public void setFromFile(String fromFile) {
		this.fromFile = fromFile;
	}

	/**
	 * @return the fromSheet
	 */
	public String getFromSheet() {
		return fromSheet;
	}

	/**
	 * @param fromSheet
	 *            the fromSheet to set
	 */
	public void setFromSheet(String fromSheet) {
		this.fromSheet = fromSheet;
	}

	/**
	 * @return the cell
	 */
	public Cell[] getCell() {
		return cell;
	}

	/**
	 * @param cell
	 *            the cell to set
	 */
	public void setCell(Cell[] cell) {
		this.cell = cell;
	}

}
