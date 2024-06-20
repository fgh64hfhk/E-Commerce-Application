package com.app.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.config.AppConstants;
import com.app.entites.ProductVariant;
import com.app.payloads.ProductVariantDTO;
import com.app.payloads.ProductVariantResponse;
import com.app.services.ProductVariantService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@CrossOrigin
public class ProductVariantController {

	@Autowired
	private ProductVariantService variantService;

	@PostMapping("/admin/product/{productId}/variant")
	public ResponseEntity<ProductVariantDTO> addVariant(@Valid @RequestBody ProductVariant productVariant,
			@PathVariable Long productId) {

		ProductVariantDTO saveProductVariant = variantService.addProductVariantById(productId, productVariant);

		return new ResponseEntity<ProductVariantDTO>(saveProductVariant, HttpStatus.CREATED);
	}

	@PostMapping("/admin/product/{productId}/variants")
	public ResponseEntity<List<ProductVariantDTO>> addVariants(@Valid @RequestBody List<ProductVariant> productVariant,
			@PathVariable Long productId) {

		List<ProductVariantDTO> savedProductVariants = variantService.addProductVariantsById(productId, productVariant);

		return new ResponseEntity<List<ProductVariantDTO>>(savedProductVariants, HttpStatus.CREATED);
	}

	@GetMapping("/public/{productId}/variants")
	public ResponseEntity<ProductVariantResponse> getAllProductVariants(@PathVariable Long productId,
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

		ProductVariantResponse productVariantResponse = variantService.getAllProductVariantsById(productId, pageNumber,
				pageSize, sortBy, sortOrder);

		return new ResponseEntity<ProductVariantResponse>(productVariantResponse, HttpStatus.FOUND);
	}

	@PutMapping("/admin/products/{productId}/variant")
	public ResponseEntity<ProductVariantDTO> updateProduct(@Valid @RequestBody ProductVariant productVariant, @PathVariable Long productId) {
		
		ProductVariantDTO updatedProductVariant = variantService.updateProductVariant(productId, productVariant);

		return new ResponseEntity<ProductVariantDTO>(updatedProductVariant, HttpStatus.OK);
	}

	@PutMapping(value = "/admin/products/{productId}/{color}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductVariantDTO> updateProductImage(@PathVariable Long productId,
			@PathVariable String color,
			@RequestParam("image") MultipartFile image) throws IOException {
		
		ProductVariantDTO updatedProductVariant = variantService.updateProductImage(productId, color, image);
		
		return new ResponseEntity<ProductVariantDTO>(updatedProductVariant, HttpStatus.OK);
	}

	@DeleteMapping("/admin/products/{productId}/{sku}")
	public ResponseEntity<String> deleteProductVariantById(@PathVariable Long productId, @PathVariable String sku) {
		
		String status = variantService.deleteProductVariantById(productId, sku);

		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

}
