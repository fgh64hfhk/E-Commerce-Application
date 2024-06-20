package com.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import com.app.entites.CartItem;
import com.app.entites.ProductVariant;

// CartItemRepository 接口
public interface CartItemRepo extends JpaRepository<CartItem, Long>, QueryByExampleExecutor<CartItem>{
	
	@Query("SELECT ci.productVariant FROM CartItem ci WHERE ci.productVariant.sku = ?1")
	ProductVariant findProductVariantBySku(String sku);
	
	@Query("SELECT ci FROM CartItem ci JOIN ci.productVariant pv WHERE pv.sku = ?1")
	List<CartItem> findCartItemsBySku(String sku);
	
//	@Query("SELECT ci.cart FROM CartItem ci WHERE ci.product.id = ?1")
//	List<Cart> findCartsByProductId(Long productId);
	
	@Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.productVariant.id = ?2")
	CartItem findCartItemByProductIdAndCartId(Long cartId, Long productId);
	
//	@Query("SELECT ci.cart FROM CartItem ci WHERE ci.cart.user.email = ?1 AND ci.cart.id = ?2")
//	Cart findCartByEmailAndCartId(String email, Integer cartId);
	
//	@Query("SELECT ci.order FROM CartItem ci WHERE ci.order.user.email = ?1 AND ci.order.id = ?2")
//	Order findOrderByEmailAndOrderId(String email, Integer orderId);
	
	@Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.productVariant.id = ?2")
    void deleteCartItemByProductIdAndCartId(Long productId, Long cartId);
}
