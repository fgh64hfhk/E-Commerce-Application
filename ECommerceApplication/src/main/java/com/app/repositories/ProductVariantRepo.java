package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.entites.ProductVariant;

public interface ProductVariantRepo extends JpaRepository<ProductVariant, Long> {

	@Query("SELECT pv FROM ProductVariant pv WHERE pv.product.id = ?1 AND pv.sku = ?2")
	ProductVariant findByProductIdAndProductVariantSKU(@Param("productId") Long productId, @Param("sku") String sku);

	@Query("SELECT v FROM ProductVariant v WHERE v.color = :color")
	ProductVariant findByProductVariantColor(@Param("color") String color);
	
}
