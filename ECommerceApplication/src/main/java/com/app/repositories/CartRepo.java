package com.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entites.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
	
	@Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.id = ?2")
	Cart findCartByEmailAndCartId(String email, Long cartId);

	@Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.productVariant pv WHERE pv.id = ?1")
	List<Cart> findCartsByProductId(Long productId);

}
