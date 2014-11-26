package com.walmart.releaseautomation.weekly.dashboard.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Filter implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String fileToFilter;
	private String sheetToFilter;
	private String colToFilter;
	private List<String> valuesToFilter;
	private boolean filterColADate;

	/**
	 * @return the fileToFilter
	 */
	public String getFileToFilter() {
		return fileToFilter;
	}

	/**
	 * @param fileToFilter
	 *            the fileToFilter to set
	 */
	public void setFileToFilter(String fileToFilter) {
		this.fileToFilter = fileToFilter;
	}

	/**
	 * @return the sheetToFilter
	 */
	public String getSheetToFilter() {
		return sheetToFilter;
	}

	/**
	 * @param sheetToFilter
	 *            the sheetToFilter to set
	 */
	public void setSheetToFilter(String sheetToFilter) {
		this.sheetToFilter = sheetToFilter;
	}

	/**
	 * @return the colToFilter
	 */
	public String getColToFilter() {
		return colToFilter;
	}

	/**
	 * @param colToFilter
	 *            the colToFilter to set
	 */
	public void setColToFilter(String colToFilter) {
		this.colToFilter = colToFilter;
	}

	/**
	 * @return the valuesToFilter
	 */
	@XmlElementWrapper(name = "values")
	public List<String> getValuesToFilter() {
		return valuesToFilter;
	}

	/**
	 * @param valuesToFilter
	 *            the valuesToFilter to set
	 */
	public void setValuesToFilter(List<String> valuesToFilter) {
		this.valuesToFilter = valuesToFilter;
	}

	/**
	 * @return the filterColADate
	 */
	public boolean isFilterColADate() {
		return filterColADate;
	}

	/**
	 * @param filterColADate the filterColADate to set
	 */
	public void setFilterColADate(boolean filterColADate) {
		this.filterColADate = filterColADate;
	}


}
