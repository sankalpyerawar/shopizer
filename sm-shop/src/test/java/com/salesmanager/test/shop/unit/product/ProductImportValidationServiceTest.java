package com.salesmanager.test.shop.unit.product;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.salesmanager.shop.model.catalog.product.importt.ProductImportRow;
import com.salesmanager.shop.model.catalog.product.importt.ValidationError;
import com.salesmanager.shop.store.api.v2.product.ProductImportValidationService;

public class ProductImportValidationServiceTest {

	private ProductImportValidationService validationService;

	@BeforeEach
	void setUp() {
		validationService = new ProductImportValidationService();
	}

	private ProductImportRow validRow(String sku) {
		ProductImportRow row = new ProductImportRow();
		row.setSku(sku);
		row.setName("Test Product");
		row.setPrice(new BigDecimal("19.99"));
		row.setQuantity(10);
		row.setVisible(true);
		row.setCanBePurchased(true);
		row.setSortOrder(0);
		return row;
	}

	@Test
	void validRows_returnsNoErrors() {
		List<ProductImportRow> rows = Arrays.asList(validRow("SKU001"), validRow("SKU002"));
		List<ValidationError> errors = validationService.validate(rows);
		assertTrue(errors.isEmpty());
	}

	@Test
	void missingSku_returnsError() {
		ProductImportRow row = validRow(null);
		row.setSku(null);
		List<ValidationError> errors = validationService.validate(Arrays.asList(row));
		assertEquals(1, errors.size());
		assertEquals("sku", errors.get(0).getField());
		assertEquals(1, errors.get(0).getRow());
	}

	@Test
	void emptySku_returnsError() {
		ProductImportRow row = validRow("");
		row.setSku("");
		List<ValidationError> errors = validationService.validate(Arrays.asList(row));
		assertFalse(errors.isEmpty());
		assertEquals("sku", errors.get(0).getField());
	}

	@Test
	void missingName_returnsError() {
		ProductImportRow row = validRow("SKU001");
		row.setName(null);
		List<ValidationError> errors = validationService.validate(Arrays.asList(row));
		assertEquals(1, errors.size());
		assertEquals("name", errors.get(0).getField());
	}

	@Test
	void nullPrice_returnsError() {
		ProductImportRow row = validRow("SKU001");
		row.setPrice(null);
		List<ValidationError> errors = validationService.validate(Arrays.asList(row));
		assertEquals(1, errors.size());
		assertEquals("price", errors.get(0).getField());
	}

	@Test
	void zeroPrice_returnsError() {
		ProductImportRow row = validRow("SKU001");
		row.setPrice(BigDecimal.ZERO);
		List<ValidationError> errors = validationService.validate(Arrays.asList(row));
		assertEquals(1, errors.size());
		assertEquals("price", errors.get(0).getField());
	}

	@Test
	void negativePrice_returnsError() {
		ProductImportRow row = validRow("SKU001");
		row.setPrice(new BigDecimal("-5.00"));
		List<ValidationError> errors = validationService.validate(Arrays.asList(row));
		assertEquals(1, errors.size());
		assertEquals("price", errors.get(0).getField());
	}

	@Test
	void negativeQuantity_returnsError() {
		ProductImportRow row = validRow("SKU001");
		row.setQuantity(-1);
		List<ValidationError> errors = validationService.validate(Arrays.asList(row));
		assertEquals(1, errors.size());
		assertEquals("quantity", errors.get(0).getField());
	}

	@Test
	void duplicateSkuInBatch_returnsError() {
		ProductImportRow row1 = validRow("SKU001");
		ProductImportRow row2 = validRow("SKU001");
		List<ValidationError> errors = validationService.validate(Arrays.asList(row1, row2));
		assertEquals(1, errors.size());
		assertEquals("sku", errors.get(0).getField());
		assertEquals(2, errors.get(0).getRow());
		assertTrue(errors.get(0).getMessage().toLowerCase().contains("duplicate"));
	}

	@Test
	void multipleErrorsOnDifferentRows() {
		ProductImportRow row1 = validRow("SKU001");
		row1.setPrice(null);
		ProductImportRow row2 = validRow(null);
		List<ValidationError> errors = validationService.validate(Arrays.asList(row1, row2));
		assertEquals(2, errors.size());
		assertEquals(1, errors.get(0).getRow());
		assertEquals(2, errors.get(1).getRow());
	}

	@Test
	void emptyList_returnsNoErrors() {
		List<ValidationError> errors = validationService.validate(new ArrayList<>());
		assertTrue(errors.isEmpty());
	}
}
