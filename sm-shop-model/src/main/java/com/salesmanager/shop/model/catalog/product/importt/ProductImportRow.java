package com.salesmanager.shop.model.catalog.product.importt;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductImportRow implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sku;
	private String name;
	private BigDecimal price;
	private int quantity;
	private boolean visible;
	private boolean canBePurchased;
	private int sortOrder;
	private String description;
	private String highlights;
	private String friendlyUrl;
	private String manufacturer;
	private String type;
	private BigDecimal weight;
	private BigDecimal height;
	private BigDecimal width;
	private BigDecimal length;
	private String categories; // comma-separated category codes

	public String getSku() { return sku; }
	public void setSku(String sku) { this.sku = sku; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public BigDecimal getPrice() { return price; }
	public void setPrice(BigDecimal price) { this.price = price; }
	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
	public boolean isVisible() { return visible; }
	public void setVisible(boolean visible) { this.visible = visible; }
	public boolean isCanBePurchased() { return canBePurchased; }
	public void setCanBePurchased(boolean canBePurchased) { this.canBePurchased = canBePurchased; }
	public int getSortOrder() { return sortOrder; }
	public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public String getHighlights() { return highlights; }
	public void setHighlights(String highlights) { this.highlights = highlights; }
	public String getFriendlyUrl() { return friendlyUrl; }
	public void setFriendlyUrl(String friendlyUrl) { this.friendlyUrl = friendlyUrl; }
	public String getManufacturer() { return manufacturer; }
	public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	public BigDecimal getWeight() { return weight; }
	public void setWeight(BigDecimal weight) { this.weight = weight; }
	public BigDecimal getHeight() { return height; }
	public void setHeight(BigDecimal height) { this.height = height; }
	public BigDecimal getWidth() { return width; }
	public void setWidth(BigDecimal width) { this.width = width; }
	public BigDecimal getLength() { return length; }
	public void setLength(BigDecimal length) { this.length = length; }
	public String getCategories() { return categories; }
	public void setCategories(String categories) { this.categories = categories; }
}
