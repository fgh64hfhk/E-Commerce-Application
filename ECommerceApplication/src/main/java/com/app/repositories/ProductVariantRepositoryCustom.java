package com.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.entites.ProductVariant;

public interface ProductVariantRepositoryCustom {
	
	Page<ProductVariant> findProductVariantsByProductId(Long productId, Pageable pageable);
}
