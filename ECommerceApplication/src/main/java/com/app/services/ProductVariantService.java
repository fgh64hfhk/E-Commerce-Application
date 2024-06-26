package com.app.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.entites.ProductVariant;
import com.app.payloads.ProductVariantDTO;
import com.app.payloads.ProductVariantResponse;

public interface ProductVariantService {

	ProductVariantDTO addProductVariantById(Long productId, ProductVariant variant);

	List<ProductVariantDTO> addProductVariantsById(Long productId, List<ProductVariant> variants);

	ProductVariantResponse getAllProductVariantsById(Long productId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder);

	ProductVariant getProductVariantBySku(Long productId, String sku);

	ProductVariantDTO updateProductVariant(Long productId, ProductVariant variant);

	ProductVariantDTO updateProductImage(Long productId, String color, MultipartFile image) throws IOException;

	String deleteProductVariantById(Long productId, String sku);
}
