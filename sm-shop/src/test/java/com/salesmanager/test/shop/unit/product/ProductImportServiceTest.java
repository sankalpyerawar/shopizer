package com.salesmanager.test.shop.unit.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.catalog.product.importt.ImportMode;
import com.salesmanager.shop.model.catalog.product.importt.ProductImportResult;
import com.salesmanager.shop.model.catalog.product.importt.ProductImportRow;
import com.salesmanager.shop.model.catalog.product.product.definition.ReadableProductDefinition;
import com.salesmanager.shop.model.entity.Entity;
import com.salesmanager.shop.store.api.v2.product.ProductImportService;
import com.salesmanager.shop.store.controller.product.facade.ProductDefinitionFacade;

@ExtendWith(MockitoExtension.class)
public class ProductImportServiceTest {

	@Mock
	private ProductDefinitionFacade productDefinitionFacade;

	@InjectMocks
	private ProductImportService importService;

	private MerchantStore store;
	private Language language;

	@BeforeEach
	void setUp() {
		store = new MerchantStore();
		store.setCode("DEFAULT");
		language = new Language();
		language.setCode("en");
	}

	private ProductImportRow validRow(String sku) {
		ProductImportRow row = new ProductImportRow();
		row.setSku(sku);
		row.setName("Test Product " + sku);
		row.setPrice(new BigDecimal("19.99"));
		row.setQuantity(10);
		row.setVisible(true);
		row.setCanBePurchased(true);
		row.setSortOrder(0);
		return row;
	}

	@Test
	void createOnly_allNew_createsAll() {
		List<ProductImportRow> rows = Arrays.asList(validRow("NEW1"), validRow("NEW2"));
		when(productDefinitionFacade.saveProductDefinition(any(), any(), any())).thenReturn(1L);

		ProductImportResult result = importService.importProducts(rows, ImportMode.CREATE_ONLY, store, language);

		assertEquals(2, result.getTotal());
		assertEquals(2, result.getCreated());
		assertEquals(0, result.getUpdated());
		assertEquals(0, result.getFailed());
		verify(productDefinitionFacade, times(2)).saveProductDefinition(any(), any(), any());
	}

	@Test
	void createOnly_existingSku_skips() {
		List<ProductImportRow> rows = Arrays.asList(validRow("EXISTING1"));
		when(productDefinitionFacade.saveProductDefinition(any(), any(), any()))
				.thenThrow(new RuntimeException("Product already exists"));

		ProductImportResult result = importService.importProducts(rows, ImportMode.CREATE_ONLY, store, language);

		assertEquals(1, result.getTotal());
		assertEquals(0, result.getCreated());
		assertEquals(1, result.getFailed());
	}

	@Test
	void updateOnly_existingSku_updates() {
		List<ProductImportRow> rows = Arrays.asList(validRow("EXISTING1"));
		when(productDefinitionFacade.getProductBySku(any(), eq("EXISTING1"), any()))
				.thenReturn(new ReadableProductDefinition());

		ProductImportResult result = importService.importProducts(rows, ImportMode.UPDATE_ONLY, store, language);

		assertEquals(1, result.getTotal());
		assertEquals(0, result.getCreated());
		assertEquals(1, result.getUpdated());
		assertEquals(0, result.getFailed());
		verify(productDefinitionFacade).update(any(), any(), any(), any());
	}

	@Test
	void updateOnly_nonExistingSku_skips() {
		List<ProductImportRow> rows = Arrays.asList(validRow("NONEXIST"));
		when(productDefinitionFacade.getProductBySku(any(), eq("NONEXIST"), any()))
				.thenReturn(null);

		ProductImportResult result = importService.importProducts(rows, ImportMode.UPDATE_ONLY, store, language);

		assertEquals(1, result.getTotal());
		assertEquals(0, result.getUpdated());
		assertEquals(1, result.getFailed());
	}

	@Test
	void createAndUpdate_mixedSkus() {
		ProductImportRow newRow = validRow("NEW1");
		ProductImportRow existingRow = validRow("EXISTING1");
		List<ProductImportRow> rows = Arrays.asList(newRow, existingRow);

		// NEW1 not found -> create
		when(productDefinitionFacade.getProductBySku(any(), eq("NEW1"), any())).thenReturn(null);
		when(productDefinitionFacade.saveProductDefinition(any(), any(), any())).thenReturn(1L);

		// EXISTING1 found -> update
		ReadableProductDefinition existing =
				new ReadableProductDefinition();
		existing.setId(99L);
		when(productDefinitionFacade.getProductBySku(any(), eq("EXISTING1"), any())).thenReturn(existing);

		ProductImportResult result = importService.importProducts(rows, ImportMode.CREATE_AND_UPDATE, store, language);

		assertEquals(2, result.getTotal());
		assertEquals(1, result.getCreated());
		assertEquals(1, result.getUpdated());
		assertEquals(0, result.getFailed());
	}

	@Test
	void partialFailure_collectsErrors() {
		ProductImportRow goodRow = validRow("GOOD");
		ProductImportRow badRow = validRow("BAD");
		List<ProductImportRow> rows = Arrays.asList(goodRow, badRow);

		when(productDefinitionFacade.saveProductDefinition(any(), any(), any()))
				.thenReturn(1L)
				.thenThrow(new RuntimeException("DB error"));

		ProductImportResult result = importService.importProducts(rows, ImportMode.CREATE_ONLY, store, language);

		assertEquals(2, result.getTotal());
		assertEquals(1, result.getCreated());
		assertEquals(1, result.getFailed());
		assertEquals(1, result.getErrors().size());
	}

	@Test
	void createOnly_withAllOptionalFields_mapsCorrectly() {
		ProductImportRow row = validRow("FULL1");
		row.setDescription("A full product");
		row.setHighlights("Best seller");
		row.setFriendlyUrl("full-product");
		row.setManufacturer("ACME");
		row.setType("GENERAL");
		row.setWeight(new BigDecimal("1.5"));
		row.setHeight(new BigDecimal("10"));
		row.setWidth(new BigDecimal("5"));
		row.setLength(new BigDecimal("8"));
		row.setCategories("cat-a, cat-b");

		when(productDefinitionFacade.saveProductDefinition(any(), any(), any())).thenReturn(1L);

		ProductImportResult result = importService.importProducts(
				Arrays.asList(row), ImportMode.CREATE_ONLY, store, language);

		assertEquals(1, result.getCreated());
		verify(productDefinitionFacade).saveProductDefinition(eq(store), argThat(product ->
				"FULL1".equals(product.getSku())
				&& "GENERAL".equals(product.getType())
				&& "ACME".equals(product.getManufacturer())
				&& product.getDescriptions().size() == 1
				&& "A full product".equals(product.getDescriptions().get(0).getDescription())
				&& "Best seller".equals(product.getDescriptions().get(0).getHighlights())
				&& "full-product".equals(product.getDescriptions().get(0).getFriendlyUrl())
				&& product.getProductSpecifications().getWeight().compareTo(new BigDecimal("1.5")) == 0
				&& product.getProductSpecifications().getHeight().compareTo(new BigDecimal("10")) == 0
				&& product.getProductSpecifications().getWidth().compareTo(new BigDecimal("5")) == 0
				&& product.getProductSpecifications().getLength().compareTo(new BigDecimal("8")) == 0
				&& product.getCategories().size() == 2
				&& "cat-a".equals(product.getCategories().get(0).getCode())
				&& "cat-b".equals(product.getCategories().get(1).getCode())
		), eq(language));
	}

	@Test
	void findBySku_exceptionReturnsNull_treatedAsNotFound() {
		ProductImportRow row = validRow("ERR_SKU");
		when(productDefinitionFacade.getProductBySku(any(), eq("ERR_SKU"), any()))
				.thenThrow(new RuntimeException("DB connection lost"));

		ProductImportResult result = importService.importProducts(
				Arrays.asList(row), ImportMode.UPDATE_ONLY, store, language);

		assertEquals(1, result.getFailed());
		assertEquals(0, result.getUpdated());
	}

	@Test
	void createAndUpdate_existingThrowsOnUpdate_collectsError() {
		ProductImportRow row = validRow("FAIL_UPD");
		ReadableProductDefinition existing = new ReadableProductDefinition();
		existing.setId(50L);
		when(productDefinitionFacade.getProductBySku(any(), eq("FAIL_UPD"), any()))
				.thenReturn(existing);
		doThrow(new RuntimeException("Update failed"))
				.when(productDefinitionFacade).update(any(), any(), any(), any());

		ProductImportResult result = importService.importProducts(
				Arrays.asList(row), ImportMode.CREATE_AND_UPDATE, store, language);

		assertEquals(1, result.getFailed());
		assertEquals(0, result.getUpdated());
		assertEquals(1, result.getErrors().size());
	}
}
