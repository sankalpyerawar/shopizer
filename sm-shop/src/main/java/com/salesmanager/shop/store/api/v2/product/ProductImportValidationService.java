package com.salesmanager.shop.store.api.v2.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.salesmanager.shop.model.catalog.product.importt.ProductImportRow;
import com.salesmanager.shop.model.catalog.product.importt.ValidationError;

@Service
public class ProductImportValidationService {

	public List<ValidationError> validate(List<ProductImportRow> rows) {
		List<ValidationError> errors = new ArrayList<>();
		Set<String> seenSkus = new HashSet<>();

		for (int i = 0; i < rows.size(); i++) {
			int rowNum = i + 1;
			ProductImportRow row = rows.get(i);

			if (StringUtils.isBlank(row.getSku())) {
				errors.add(new ValidationError(rowNum, "sku", "SKU is required", "Provide a unique SKU"));
				continue;
			}

			if (!seenSkus.add(row.getSku())) {
				errors.add(new ValidationError(rowNum, "sku",
						"Duplicate SKU '" + row.getSku() + "' in CSV", "Each SKU must be unique within the file"));
				continue;
			}

			if (StringUtils.isBlank(row.getName())) {
				errors.add(new ValidationError(rowNum, "name", "Name is required", "Provide a product name"));
			}

			if (row.getPrice() == null || row.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
				errors.add(new ValidationError(rowNum, "price", "Price must be greater than 0", "Enter a positive price"));
			}

			if (row.getQuantity() < 0) {
				errors.add(new ValidationError(rowNum, "quantity", "Quantity must be >= 0", "Enter a non-negative quantity"));
			}
		}

		return errors;
	}
}
