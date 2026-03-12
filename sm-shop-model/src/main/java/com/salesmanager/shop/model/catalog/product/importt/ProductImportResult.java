package com.salesmanager.shop.model.catalog.product.importt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductImportResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private int total;
	private int created;
	private int updated;
	private int failed;
	private List<ValidationError> errors = new ArrayList<>();

	public int getTotal() { return total; }
	public void setTotal(int total) { this.total = total; }
	public int getCreated() { return created; }
	public void setCreated(int created) { this.created = created; }
	public int getUpdated() { return updated; }
	public void setUpdated(int updated) { this.updated = updated; }
	public int getFailed() { return failed; }
	public void setFailed(int failed) { this.failed = failed; }
	public List<ValidationError> getErrors() { return errors; }
	public void setErrors(List<ValidationError> errors) { this.errors = errors; }
}
