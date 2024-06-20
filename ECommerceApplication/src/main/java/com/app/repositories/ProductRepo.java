package com.app.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.entites.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

	@Query("select p from Product p where p.productName like %?1%")
	Page<Product> findByProductNameLike(@Param("keyword") String keyword, Pageable pageDetails);

	@Query("SELECT p FROM Product p JOIN FETCH p.coupons c WHERE c.id = ?1")
	List<Product> findProductsByCouponId(Long couponId);
}
