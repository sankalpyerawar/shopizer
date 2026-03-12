package com.salesmanager.shop.store.api.v2.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.catalog.category.Category;
import com.salesmanager.shop.model.catalog.product.ProductDescription;
import com.salesmanager.shop.model.catalog.product.importt.ImportMode;
import com.salesmanager.shop.model.catalog.product.importt.ProductImportResult;
import com.salesmanager.shop.model.catalog.product.importt.ProductImportRow;
import com.salesmanager.shop.model.catalog.product.importt.ValidationError;
import com.salesmanager.shop.model.catalog.product.product.ProductSpecification;
import com.salesmanager.shop.model.catalog.product.product.definition.PersistableProductDefinition;
import com.salesmanager.shop.model.catalog.product.product.definition.ReadableProductDefinition;
import com.salesmanager.shop.store.controller.product.facade.ProductDefinitionFacade;

@Service
public class ProductImportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductImportService.class);

	@Autowired
	private ProductDefinitionFacade productDefinitionFacade;

	public ProductImportResult importProducts(List<ProductImportRow> rows, ImportMode mode,
			MerchantStore store, Language language) {

		ProductImportResult result = new ProductImportResult();
		result.setTotal(rows.size());

		for (int i = 0; i < rows.size(); i++) {
			ProductImportRow row = rows.get(i);
			try {
				switch (mode) {
					case CREATE_ONLY:
						createProduct(row, store, language);
						result.setCreated(result.getCreated() + 1);
						break;
					case UPDATE_ONLY:
						if (updateProduct(row, store, language)) {
							result.setUpdated(result.getUpdated() + 1);
						} else {
							result.setFailed(result.getFailed() + 1);
							result.getErrors().add(new ValidationError(i + 1, "sku",
									"SKU '" + row.getSku() + "' not found for update", "Use CREATE_AND_UPDATE mode"));
						}
						break;
					case CREATE_AND_UPDATE:
						ReadableProductDefinition existing = findBySku(row.getSku(), store, language);
						if (existing != null) {
							PersistableProductDefinition product = mapRowToProduct(row, language.getCode());
							productDefinitionFacade.update(existing.getId(), product, store, language);
							result.setUpdated(result.getUpdated() + 1);
						} else {
							createProduct(row, store, language);
							result.setCreated(result.getCreated() + 1);
						}
						break;
				}
			} catch (Exception e) {
				LOGGER.warn("Import failed for row {} (SKU: {}): {}", i + 1, row.getSku(), e.getMessage());
				result.setFailed(result.getFailed() + 1);
				result.getErrors().add(new ValidationError(i + 1, "sku",
						"Import failed: " + e.getMessage(), null));
			}
		}

		LOGGER.info("Import complete: total={}, created={}, updated={}, failed={}",
				result.getTotal(), result.getCreated(), result.getUpdated(), result.getFailed());
		return result;
	}

	private void createProduct(ProductImportRow row, MerchantStore store, Language language) {
		PersistableProductDefinition product = mapRowToProduct(row, language.getCode());
		product.setId(null);
		productDefinitionFacade.saveProductDefinition(store, product, language);
	}

	private boolean updateProduct(ProductImportRow row, MerchantStore store, Language language) {
		ReadableProductDefinition existing = findBySku(row.getSku(), store, language);
		if (existing == null) {
			return false;
		}
		PersistableProductDefinition product = mapRowToProduct(row, language.getCode());
		productDefinitionFacade.update(existing.getId(), product, store, language);
		return true;
	}

	private ReadableProductDefinition findBySku(String sku, MerchantStore store, Language language) {
		try {
			return productDefinitionFacade.getProductBySku(store, sku, language);
		} catch (Exception e) {
			return null;
		}
	}

	private PersistableProductDefinition mapRowToProduct(ProductImportRow row, String langCode) {
		PersistableProductDefinition product = new PersistableProductDefinition();
		product.setIdentifier(row.getSku());
		product.setSku(row.getSku());
		product.setPrice(row.getPrice());
		product.setQuantity(row.getQuantity());
		product.setVisible(row.isVisible());
		product.setCanBePurchased(row.isCanBePurchased());
		product.setSortOrder(row.getSortOrder());

		if (StringUtils.isNotBlank(row.getType())) {
			product.setType(row.getType());
		}
		if (StringUtils.isNotBlank(row.getManufacturer())) {
			product.setManufacturer(row.getManufacturer());
		}

		ProductDescription desc = new ProductDescription();
		desc.setName(row.getName());
		desc.setLanguage(langCode);
		if (StringUtils.isNotBlank(row.getDescription())) {
			desc.setDescription(row.getDescription());
		}
		if (StringUtils.isNotBlank(row.getHighlights())) {
			desc.setHighlights(row.getHighlights());
		}
		if (StringUtils.isNotBlank(row.getFriendlyUrl())) {
			desc.setFriendlyUrl(row.getFriendlyUrl());
		}
		product.getDescriptions().add(desc);

		ProductSpecification specs = new ProductSpecification();
		if (row.getWeight() != null) specs.setWeight(row.getWeight());
		if (row.getHeight() != null) specs.setHeight(row.getHeight());
		if (row.getWidth() != null) specs.setWidth(row.getWidth());
		if (row.getLength() != null) specs.setLength(row.getLength());
		product.setProductSpecifications(specs);

		if (StringUtils.isNotBlank(row.getCategories())) {
			List<Category> categories = new ArrayList<>();
			for (String code : row.getCategories().split(",")) {
				String trimmed = code.trim();
				if (!trimmed.isEmpty()) {
					Category cat = new Category();
					cat.setCode(trimmed);
					categories.add(cat);
				}
			}
			product.setCategories(categories);
		}

		return product;
	}
}
