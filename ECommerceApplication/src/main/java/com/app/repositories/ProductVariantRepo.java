package com.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.entites.ProductVariant;

public interface ProductVariantRepo extends JpaRepository<ProductVariant, Long>, ProductVariantRepositoryCustom {

	@Override
	default Page<ProductVariant> findProductVariantsByProductId(Long productId, Pageable pageable) {
		return findAllByProduct_Id(productId, pageable);
	}

	@Query("SELECT pv FROM ProductVariant pv JOIN pv.product p WHERE p.id = ?1")
	Page<ProductVariant> findAllByProduct_Id(Long productId, Pageable pageable);

	@Query("SELECT pv FROM ProductVariant pv WHERE pv.product.id = ?1 AND pv.sku = ?2")
	ProductVariant findByProductIdAndProductVariantSKU(@Param("productId") Long productId, @Param("sku") String sku);

	@Query("SELECT v FROM ProductVariant v WHERE v.color = :color")
	ProductVariant findByProductVariantColor(@Param("color") String color);

}
