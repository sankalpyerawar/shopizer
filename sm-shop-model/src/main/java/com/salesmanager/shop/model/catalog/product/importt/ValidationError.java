package com.salesmanager.shop.model.catalog.product.importt;

import java.io.Serializable;

public class ValidationError implements Serializable {

	private static final long serialVersionUID = 1L;

	private int row;
	private String field;
	private String message;
	private String suggestion;

	public ValidationError() {}

	public ValidationError(int row, String field, String message, String suggestion) {
		this.row = row;
		this.field = field;
		this.message = message;
		this.suggestion = suggestion;
	}

	public int getRow() { return row; }
	public void setRow(int row) { this.row = row; }
	public String getField() { return field; }
	public void setField(String field) { this.field = field; }
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	public String getSuggestion() { return suggestion; }
	public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
}
