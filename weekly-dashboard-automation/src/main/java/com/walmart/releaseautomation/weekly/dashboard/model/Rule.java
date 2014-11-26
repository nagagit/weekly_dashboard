package com.walmart.releaseautomation.weekly.dashboard.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Rule implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private Filter filter;
	private Update update;
	private Calculate calculate;
	private CopyData copyData;
	private DeleteData deleteData;
	private EvaluateFormula evaluateFormula;
	private ExecuteMacro executeMacro;
	private FormatColumn formatColumn;
	private boolean isFilterColADate;
	private RowCount count;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the filter
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	/**
	 * @return the update
	 */
	public Update getUpdate() {
		return update;
	}

	/**
	 * @param update
	 *            the update to set
	 */
	public void setUpdate(Update update) {
		this.update = update;
	}

	/**
	 * @return the calculate
	 */
	public Calculate getCalculate() {
		return calculate;
	}

	/**
	 * @param calculate
	 *            the calculate to set
	 */
	public void setCalculate(Calculate calculate) {
		this.calculate = calculate;
	}

	/**
	 * @return the copyData
	 */
	public CopyData getCopyData() {
		return copyData;
	}

	/**
	 * @param copyData
	 *            the copyData to set
	 */
	public void setCopyData(CopyData copyData) {
		this.copyData = copyData;
	}

	/**
	 * @return the deleteData
	 */
	public DeleteData getDeleteData() {
		return deleteData;
	}

	/**
	 * @param deleteData
	 *            the deleteData to set
	 */
	public void setDeleteData(DeleteData deleteData) {
		this.deleteData = deleteData;
	}

	/**
	 * @return the evaluateFormula
	 */
	public EvaluateFormula getEvaluateFormula() {
		return evaluateFormula;
	}

	/**
	 * @param evaluateFormula
	 *            the evaluateFormula to set
	 */
	public void setEvaluateFormula(EvaluateFormula evaluateFormula) {
		this.evaluateFormula = evaluateFormula;
	}

	/**
	 * @return the executeMacro
	 */
	public ExecuteMacro getExecuteMacro() {
		return executeMacro;
	}

	/**
	 * @param executeMacro
	 *            the executeMacro to set
	 */
	public void setExecuteMacro(ExecuteMacro executeMacro) {
		this.executeMacro = executeMacro;
	}

	/**
	 * @return the formatColumn
	 */
	public FormatColumn getFormatColumn() {
		return formatColumn;
	}

	/**
	 * @param formatColumn
	 *            the formatColumn to set
	 */
	public void setFormatColumn(FormatColumn formatColumn) {
		this.formatColumn = formatColumn;
	}

	/**
	 * @return the isFilterColADate
	 */
	public boolean isFilterColADate() {
		return isFilterColADate;
	}

	/**
	 * @param isFilterColADate the isFilterColADate to set
	 */
	public void setFilterColADate(boolean isFilterColADate) {
		this.isFilterColADate = isFilterColADate;
	}

	/**
	 * @return the count
	 */
	public RowCount getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(RowCount count) {
		this.count = count;
	}

}
