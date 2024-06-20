package com.app.services;

import java.util.List;

import com.app.entites.CartItem;
import com.app.entites.ProductVariant;
import com.app.payloads.CartDTO;

public interface CartService {
	
	CartDTO addProductToCart(Long cartId, Long productId, Integer quantity, String sku);
	
	List<CartDTO> getAllCarts();
	
	CartDTO getCart(String emailId, Long cartId);
	
	CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity, String sku);
	
	void updateProductInCarts(Long cartId, Long productId, String sku, ProductVariant productVariant);
	
	String deleteProductFromCart(Long cartId, Long productId, String sku);
	
	List<CartItem> findCartItemsBySku(String sku);
	
}
