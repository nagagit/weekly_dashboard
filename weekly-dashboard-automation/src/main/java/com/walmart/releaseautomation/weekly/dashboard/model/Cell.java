package com.walmart.releaseautomation.weekly.dashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Cell {
	private String header;
	private String formula;
	private String cellType;

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header
	 *            the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @param formula
	 *            the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @return the cellType
	 */
	public String getCellType() {
		return cellType;
	}

	/**
	 * @param cellType
	 *            the cellType to set
	 */
	public void setCellType(String cellType) {
		this.cellType = cellType;
	}

}
