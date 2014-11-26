package com.walmart.releaseautomation.weekly.dashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FormatColumn {
	private String fromFile;
	private String sheet;
	private String[] onColumns;
	private String cellTypeformat;

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
	 * @return the cellTypeformat
	 */
	public String getCellTypeformat() {
		return cellTypeformat;
	}

	/**
	 * @param cellTypeformat
	 *            the cellTypeformat to set
	 */
	public void setCellTypeformat(String cellTypeformat) {
		this.cellTypeformat = cellTypeformat;
	}

	/**
	 * @return the sheet
	 */
	public String getSheet() {
		return sheet;
	}

	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}

	/**
	 * @return the onColumns
	 */
	public String[] getOnColumns() {
		return onColumns;
	}

	/**
	 * @param onColumns the onColumns to set
	 */
	public void setOnColumns(String[] onColumns) {
		this.onColumns = onColumns;
	}

}
