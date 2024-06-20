package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Product;
import com.app.entites.ProductVariant;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CartDTO;
import com.app.payloads.ProductDTO;
import com.app.payloads.ProductVariantDTO;
import com.app.repositories.CartItemRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.ProductRepo;
import com.app.repositories.ProductVariantRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class CartServiceImpl implements CartService {

//	@Autowired
//	private CartRepo cartRepo;
//
//	@Autowired
//	private ProductRepo productRepo;
//
//	@Autowired
//	private ProductVariantRepo productVariantRepo;
//
//	@Autowired
//	private CartItemRepo cartItemRepo;
//
//	@Autowired
//	private ModelMapper modelMapper;
//
//	@Override
//	public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity, String sku) {
//
//		Cart cart = cartRepo.findById(cartId)
//				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
//
//		Product product = productRepo.findById(productId)
//				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
//
//		ProductVariant variant = productVariantRepo.findByProductIdAndProductVariantSKU(productId, sku);
//
//		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
//
//		if (cartItem != null) {
//			throw new APIException("Product " + product.getProductName() + " already exists in the cart");
//		}
//
//		if (product.getQuantity() == 0) {
//			throw new APIException(product.getProductName() + " is not available");
//		}
//
//		if (variant.getInventory() < quantity) {
//			throw new APIException("Please, make an order of the " + product.getProductName()
//					+ " less than or equal to the quantity " + variant.getInventory() + ".");
//		}
//
//		CartItem newCartItem = new CartItem();
//
//		newCartItem.setProductVariant(variant);
//		newCartItem.setCart(cart);
//		newCartItem.setQuantity(quantity);
//		newCartItem.setDiscount(product.getDiscount());
//		newCartItem.setProductPrice(product.getSpecialPrice());
//
//		cartItemRepo.save(newCartItem);
//
////		product.setQuantity(product.getQuantity() - quantity);
//		variant.setInventory(variant.getInventory() - quantity);
//
//		cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
//
//		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//		List<ProductVariantDTO> productVariantDTOs = cart.getCartItems().stream()
//				.map(p -> modelMapper.map(p.getProductVariant(), ProductVariantDTO.class)).collect(Collectors.toList());
//
//		cartDTO.setProductVariants(productVariantDTOs);
//
//		return cartDTO;
//
//	}
//
//	@Override
//	public List<CartDTO> getAllCarts() {
//		List<Cart> carts = cartRepo.findAll();
//
//		if (carts.size() == 0) {
//			throw new APIException("No cart exists");
//		}
//
//		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
//			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//			List<ProductVariantDTO> variantDTOs = cart.getCartItems().stream()
//					.map(p -> modelMapper.map(p.getProductVariant(), ProductVariantDTO.class)).collect(Collectors.toList());
//
//			cartDTO.setProductVariants(variantDTOs);
//
//			return cartDTO;
//
//		}).collect(Collectors.toList());
//
//		return cartDTOs;
//	}
//
//	@Override
//	public CartDTO getCart(String emailId, Long cartId) {
//		Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);
//
//		if (cart == null) {
//			throw new ResourceNotFoundException("Cart", "cartId", cartId);
//		}
//
//		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//		List<ProductVariantDTO> variantDTOs = cart.getCartItems().stream()
//				.map(p -> modelMapper.map(p.getProductVariant(), ProductVariantDTO.class)).collect(Collectors.toList());
//
//		cartDTO.setProductVariants(variantDTOs);
//
//		return cartDTO;
//	}
//
//	@Override
//	public List<CartItem> findCartItemsBySku(String sku) {
//		// 創建商品變體範例
//		ProductVariant productVariant = new ProductVariant();
//		productVariant.setSku(sku);
//
//		// 創建購物車項目範例
//		CartItem cartItem = new CartItem();
//		cartItem.setProductVariant(productVariant);
//
//		// 創建範例匹配器，只匹配 product variant sku
//		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("productVariant.sku",
//				ExampleMatcher.GenericPropertyMatchers.exact());
//
//		// 創建範例
//		Example<CartItem> example = Example.of(cartItem, matcher);
//
//		// 查詢符合條件的購物車項目
//		return cartItemRepo.findAll(example);
//	}
//
//	@Override
//	public void updateProductInCarts(Long cartId, Long productId) {
//		Cart cart = cartRepo.findById(cartId)
//				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
//
//		Product product = productRepo.findById(productId)
//				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
//
//		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
//
//		if (cartItem == null) {
//			throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
//		}
//
//		double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
//
//		cartItem.setProductPrice(product.getSpecialPrice());
//
//		cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));
//
//		cartItem = cartItemRepo.save(cartItem);
//	}
//
//	@Override
//	public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) {
//		Cart cart = cartRepo.findById(cartId)
//				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
//
//		Product product = productRepo.findById(productId)
//				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
//
//		if (product.getQuantity() == 0) {
//			throw new APIException(product.getProductName() + " is not available");
//		}
//
//		if (product.getQuantity() < quantity) {
//			throw new APIException("Please, make an order of the " + product.getProductName()
//					+ " less than or equal to the quantity " + product.getQuantity() + ".");
//		}
//
//		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
//
//		if (cartItem == null) {
//			throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
//		}
//
//		double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
//
//		product.setQuantity(product.getQuantity() + cartItem.getQuantity() - quantity);
//
//		cartItem.setProductPrice(product.getSpecialPrice());
//		cartItem.setQuantity(quantity);
//		cartItem.setDiscount(product.getDiscount());
//
//		cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * quantity));
//
//		cartItem = cartItemRepo.save(cartItem);
//
//		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//		List<ProductVariantDTO> variantDTOs = cart.getCartItems().stream()
//				.map(p -> modelMapper.map(p.getProductVariant(), ProductVariantDTO.class)).collect(Collectors.toList());
//
//		cartDTO.setProductVariants(variantDTOs);
//
//		return cartDTO;
//
//	}
//
//	@Override
//	public String deleteProductFromCart(Long cartId, Long productId) {
//		Cart cart = cartRepo.findById(cartId)
//				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
//
//		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
//
//		if (cartItem == null) {
//			throw new ResourceNotFoundException("Product", "productId", productId);
//		}
//
//		cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
//
//		Product product = cartItem.getProduct();
//		product.setQuantity(product.getQuantity() + cartItem.getQuantity());
//
//		cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);
//
//		return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
//	}

	@Override
	public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity, String sku) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateProductInCarts(Long cartId, Long productId, String sku, ProductVariant productVariant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String deleteProductFromCart(Long cartId, Long productId, String sku) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity, String sku) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CartDTO> getAllCarts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CartDTO getCart(String emailId, Long cartId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CartItem> findCartItemsBySku(String sku) {
		// TODO Auto-generated method stub
		return null;
	}

}
