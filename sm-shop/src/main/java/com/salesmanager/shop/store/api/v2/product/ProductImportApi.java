package com.salesmanager.shop.store.api.v2.product;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.catalog.product.importt.ImportMode;
import com.salesmanager.shop.model.catalog.product.importt.ProductImportResult;
import com.salesmanager.shop.model.catalog.product.importt.ProductImportRow;
import com.salesmanager.shop.model.catalog.product.importt.ValidationError;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/api/v2")
@Api(tags = { "Product bulk import (CSV)" })
public class ProductImportApi {

	@Autowired
	private ProductImportValidationService validationService;

	@Autowired
	private ProductImportService importService;

	private static final String CSV_TEMPLATE =
			"sku,name,price,quantity,visible,canBePurchased,sortOrder,description,highlights,friendlyUrl,manufacturer,type,weight,height,width,length,categories\n"
			+ "SKU001,Widget A,29.99,100,true,true,1,A great widget,Best seller,widget-a,ACME,GENERAL,0.5,10,5,5,cat-electronics\n"
			+ "SKU002,Widget B,49.99,50,true,true,2,Premium widget,Top rated,widget-b,ACME,GENERAL,1.0,15,8,8,\"cat-electronics,cat-premium\"\n"
			+ "SKU003,Gadget C,9.99,200,true,true,3,Budget gadget,,gadget-c,,GENERAL,0.2,5,3,3,cat-gadgets\n";

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/private/product/import/validate", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "store", dataType = "String", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "String", defaultValue = "en") })
	public @ResponseBody List<ValidationError> validate(
			@RequestBody List<ProductImportRow> rows,
			@ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language) {
		return validationService.validate(rows);
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/private/product/import/execute", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "store", dataType = "String", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "String", defaultValue = "en") })
	public @ResponseBody ProductImportResult execute(
			@RequestBody List<ProductImportRow> rows,
			@RequestParam(value = "mode", defaultValue = "CREATE_ONLY") ImportMode mode,
			@ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language) {
		return importService.importProducts(rows, mode, merchantStore, language);
	}

	@GetMapping(value = "/private/product/import/template", produces = "text/csv")
	public void downloadTemplate(HttpServletResponse response) throws Exception {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"product-import-template.csv\"");
		response.getWriter().write(CSV_TEMPLATE);
		response.getWriter().flush();
	}
}
