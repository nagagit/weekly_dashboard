/**
 * 
 */
package com.walmart.releaseautomation.weekly.dashboard.model;

/**
 * @author l.naga rajesh
 * 
 */
public class RowCount {
	private String countFromCol;
	private String countForValues;
	private boolean storeCountInMap;

	/**
	 * @return the countFromCol
	 */
	public String getCountFromCol() {
		return countFromCol;
	}

	/**
	 * @param countFromCol
	 *            the countFromCol to set
	 */
	public void setCountFromCol(String countFromCol) {
		this.countFromCol = countFromCol;
	}

	/**
	 * @return the countForValues
	 */
	public String getCountForValues() {
		return countForValues;
	}

	/**
	 * @param countForValues
	 *            the countForValues to set
	 */
	public void setCountForValues(String countForValues) {
		this.countForValues = countForValues;
	}

	/**
	 * @return the storeCountInMap
	 */
	public boolean isStoreCountInMap() {
		return storeCountInMap;
	}

	/**
	 * @param storeCountInMap the storeCountInMap to set
	 */
	public void setStoreCountInMap(boolean storeCountInMap) {
		this.storeCountInMap = storeCountInMap;
	}

}
