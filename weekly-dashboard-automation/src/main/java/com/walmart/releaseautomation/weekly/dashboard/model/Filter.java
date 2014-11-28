package com.walmart.releaseautomation.weekly.dashboard.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Filter implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String fileToFilter;
	private String sheetToFilter;
	private List<NarrowDownTo> narrowDownTos;

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
	 * @return the narrowDownTos
	 */
	public List<NarrowDownTo> getNarrowDownTos() {
		return narrowDownTos;
	}

	/**
	 * @param narrowDownTos the narrowDownTos to set
	 */
	public void setNarrowDownTos(List<NarrowDownTo> narrowDownTos) {
		this.narrowDownTos = narrowDownTos;
	}


}
