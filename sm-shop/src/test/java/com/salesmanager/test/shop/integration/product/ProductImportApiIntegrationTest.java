package com.salesmanager.test.shop.integration.product;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.shop.application.ShopApplication;
import com.salesmanager.shop.model.catalog.product.importt.ProductImportResult;
import com.salesmanager.shop.model.catalog.product.importt.ProductImportRow;
import com.salesmanager.shop.model.catalog.product.importt.ValidationError;
import com.salesmanager.test.shop.common.ServicesTestSupport;

@SpringBootTest(classes = ShopApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ProductImportApiIntegrationTest extends ServicesTestSupport {

    private static final String VALIDATE_URL = "/api/v2/private/product/import/validate?store=" + Constants.DEFAULT_STORE;
    private static final String EXECUTE_URL  = "/api/v2/private/product/import/execute?store=" + Constants.DEFAULT_STORE;
    private static final String TEMPLATE_URL = "/api/v2/private/product/import/template?store=" + Constants.DEFAULT_STORE;

    // ── helpers ──────────────────────────────────────────────────────────────

    private ProductImportRow validRow(String sku) {
        ProductImportRow row = new ProductImportRow();
        row.setSku(sku);
        row.setName("Product " + sku);
        row.setPrice(new BigDecimal("19.99"));
        row.setQuantity(10);
        row.setVisible(true);
        row.setCanBePurchased(true);
        row.setSortOrder(1);
        return row;
    }

    private List<ValidationError> callValidate(List<ProductImportRow> rows) {
        HttpEntity<List<ProductImportRow>> entity = new HttpEntity<>(rows, getHeader());
        ResponseEntity<List<ValidationError>> response = testRestTemplate.exchange(
                VALIDATE_URL, HttpMethod.POST, entity,
                new ParameterizedTypeReference<List<ValidationError>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    private ProductImportResult callExecute(List<ProductImportRow> rows, String mode) {
        HttpEntity<List<ProductImportRow>> entity = new HttpEntity<>(rows, getHeader());
        ResponseEntity<ProductImportResult> response = testRestTemplate.exchange(
                EXECUTE_URL + "&mode=" + mode, HttpMethod.POST, entity,
                ProductImportResult.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    // ── validate endpoint ─────────────────────────────────────────────────────

    @Test
    public void validate_validRows_returnsNoErrors() {
        List<ProductImportRow> rows = new ArrayList<>();
        rows.add(validRow("VALID-SKU-001"));
        rows.add(validRow("VALID-SKU-002"));

        List<ValidationError> errors = callValidate(rows);

        assertNotNull(errors);
        assertTrue("Expected no validation errors", errors.isEmpty());
    }

    @Test
    public void validate_missingSku_returnsError() {
        ProductImportRow row = validRow(null);
        row.setSku(null);

        List<ValidationError> errors = callValidate(List.of(row));

        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("sku", errors.get(0).getField());
    }

    @Test
    public void validate_missingName_returnsError() {
        ProductImportRow row = validRow("NO-NAME-SKU");
        row.setName(null);

        List<ValidationError> errors = callValidate(List.of(row));

        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("name", errors.get(0).getField());
    }

    @Test
    public void validate_zeroPriceRow_returnsError() {
        ProductImportRow row = validRow("ZERO-PRICE-SKU");
        row.setPrice(BigDecimal.ZERO);

        List<ValidationError> errors = callValidate(List.of(row));

        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("price", errors.get(0).getField());
    }

    @Test
    public void validate_negativeQuantity_returnsError() {
        ProductImportRow row = validRow("NEG-QTY-SKU");
        row.setQuantity(-1);

        List<ValidationError> errors = callValidate(List.of(row));

        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("quantity", errors.get(0).getField());
    }

    @Test
    public void validate_duplicateSkuInBatch_returnsError() {
        List<ProductImportRow> rows = new ArrayList<>();
        rows.add(validRow("DUP-SKU"));
        rows.add(validRow("DUP-SKU")); // duplicate

        List<ValidationError> errors = callValidate(rows);

        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals(2, errors.get(0).getRow()); // second row flagged
        assertEquals("sku", errors.get(0).getField());
    }

    // ── execute endpoint — CREATE_ONLY ────────────────────────────────────────

    @Test
    public void execute_createOnly_newProducts_allCreated() {
        List<ProductImportRow> rows = new ArrayList<>();
        rows.add(validRow("IMPORT-CREATE-001"));
        rows.add(validRow("IMPORT-CREATE-002"));

        ProductImportResult result = callExecute(rows, "CREATE_ONLY");

        assertNotNull(result);
        assertEquals(2, result.getTotal());
        assertEquals(2, result.getCreated());
        assertEquals(0, result.getFailed());
    }

    @Test
    public void execute_createOnly_existingSku_skipsAndFails() {
        // First import to seed the SKU
        callExecute(List.of(validRow("IMPORT-EXISTING-SKU")), "CREATE_ONLY");

        // Second import with same SKU in CREATE_ONLY mode — should fail/skip
        ProductImportResult result = callExecute(List.of(validRow("IMPORT-EXISTING-SKU")), "CREATE_ONLY");

        assertNotNull(result);
        assertEquals(1, result.getTotal());
        // created should be 0 since SKU already exists; failed or skipped
        assertEquals(0, result.getCreated());
    }

    // ── execute endpoint — UPDATE_ONLY ────────────────────────────────────────

    @Test
    public void execute_updateOnly_nonExistentSku_fails() {
        ProductImportResult result = callExecute(List.of(validRow("NONEXISTENT-SKU-XYZ")), "UPDATE_ONLY");

        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(0, result.getUpdated());
        assertEquals(1, result.getFailed());
        assertNotNull(result.getErrors());
        assertEquals(1, result.getErrors().size());
    }

    @Test
    public void execute_updateOnly_existingSku_updatesSuccessfully() {
        String sku = "IMPORT-UPDATE-SKU";
        // Seed
        callExecute(List.of(validRow(sku)), "CREATE_ONLY");

        // Update with new price
        ProductImportRow updated = validRow(sku);
        updated.setPrice(new BigDecimal("99.99"));

        ProductImportResult result = callExecute(List.of(updated), "UPDATE_ONLY");

        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getUpdated());
        assertEquals(0, result.getFailed());
    }

    // ── execute endpoint — CREATE_AND_UPDATE ─────────────────────────────────

    @Test
    public void execute_createAndUpdate_mixedBatch_createsAndUpdates() {
        String existingSku = "IMPORT-MIXED-EXISTING";
        String newSku = "IMPORT-MIXED-NEW";

        // Seed existing
        callExecute(List.of(validRow(existingSku)), "CREATE_ONLY");

        List<ProductImportRow> rows = new ArrayList<>();
        rows.add(validRow(existingSku)); // should update
        rows.add(validRow(newSku));      // should create

        ProductImportResult result = callExecute(rows, "CREATE_AND_UPDATE");

        assertNotNull(result);
        assertEquals(2, result.getTotal());
        assertEquals(1, result.getCreated());
        assertEquals(1, result.getUpdated());
        assertEquals(0, result.getFailed());
    }

    // ── template endpoint ─────────────────────────────────────────────────────

    @Test
    public void downloadTemplate_returnsCsvWithHeaders() {
        HttpEntity<Void> entity = new HttpEntity<>(getHeader());
        ResponseEntity<String> response = testRestTemplate.exchange(
                TEMPLATE_URL, HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("sku"));
        assertTrue(response.getBody().contains("name"));
        assertTrue(response.getBody().contains("price"));
        String contentDisposition = response.getHeaders().getFirst("Content-Disposition");
        assertNotNull(contentDisposition);
        assertTrue(contentDisposition.contains("product-import-template.csv"));
    }
}
