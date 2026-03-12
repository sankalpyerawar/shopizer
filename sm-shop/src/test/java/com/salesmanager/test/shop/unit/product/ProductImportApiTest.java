package com.salesmanager.test.shop.unit.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import com.salesmanager.shop.model.catalog.product.importt.ValidationError;
import com.salesmanager.shop.store.api.v2.product.ProductImportApi;
import com.salesmanager.shop.store.api.v2.product.ProductImportService;
import com.salesmanager.shop.store.api.v2.product.ProductImportValidationService;

@ExtendWith(MockitoExtension.class)
public class ProductImportApiTest {

	@Mock
	private ProductImportValidationService validationService;

	@Mock
	private ProductImportService importService;

	@InjectMocks
	private ProductImportApi api;

	@Test
	void validate_delegatesToValidationService() {
		List<ProductImportRow> rows = Arrays.asList(new ProductImportRow());
		List<ValidationError> expected = Arrays.asList(new ValidationError(1, "sku", "required", null));
		when(validationService.validate(rows)).thenReturn(expected);

		List<ValidationError> result = api.validate(rows, new MerchantStore(), new Language());

		assertEquals(expected, result);
		verify(validationService).validate(rows);
	}

	@Test
	void validate_noErrors_returnsEmptyList() {
		List<ProductImportRow> rows = Arrays.asList(new ProductImportRow());
		when(validationService.validate(rows)).thenReturn(Collections.emptyList());

		List<ValidationError> result = api.validate(rows, new MerchantStore(), new Language());

		assertTrue(result.isEmpty());
	}

	@Test
	void execute_delegatesToImportService() {
		List<ProductImportRow> rows = Arrays.asList(new ProductImportRow());
		MerchantStore store = new MerchantStore();
		Language lang = new Language();
		ProductImportResult expected = new ProductImportResult();
		expected.setTotal(1);
		expected.setCreated(1);
		when(importService.importProducts(rows, ImportMode.CREATE_ONLY, store, lang)).thenReturn(expected);

		ProductImportResult result = api.execute(rows, ImportMode.CREATE_ONLY, store, lang);

		assertEquals(1, result.getTotal());
		assertEquals(1, result.getCreated());
		verify(importService).importProducts(rows, ImportMode.CREATE_ONLY, store, lang);
	}

	@Test
	void execute_updateMode_passesCorrectMode() {
		List<ProductImportRow> rows = Arrays.asList(new ProductImportRow());
		MerchantStore store = new MerchantStore();
		Language lang = new Language();
		ProductImportResult expected = new ProductImportResult();
		when(importService.importProducts(rows, ImportMode.UPDATE_ONLY, store, lang)).thenReturn(expected);

		api.execute(rows, ImportMode.UPDATE_ONLY, store, lang);

		verify(importService).importProducts(rows, ImportMode.UPDATE_ONLY, store, lang);
	}

	@Test
	void downloadTemplate_writesCsvToResponse() throws Exception {
		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		api.downloadTemplate(response);

		verify(response).setContentType("text/csv");
		verify(response).setHeader(eq("Content-Disposition"), contains("product-import-template.csv"));
		String output = sw.toString();
		assertTrue(output.startsWith("sku,name,price"));
		assertTrue(output.contains("SKU001"));
		assertTrue(output.contains("SKU002"));
		assertTrue(output.contains("SKU003"));
	}
}
