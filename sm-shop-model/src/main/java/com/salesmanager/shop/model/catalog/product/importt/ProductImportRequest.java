package com.salesmanager.shop.model.catalog.product.importt;

import java.io.Serializable;
import java.util.List;

public class ProductImportRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ProductImportRow> rows;
	private ImportMode mode = ImportMode.CREATE_ONLY;

	public List<ProductImportRow> getRows() { return rows; }
	public void setRows(List<ProductImportRow> rows) { this.rows = rows; }
	public ImportMode getMode() { return mode; }
	public void setMode(ImportMode mode) { this.mode = mode; }
}
