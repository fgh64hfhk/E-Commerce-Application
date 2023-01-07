package com.app.services;

import java.util.List;

import com.app.entites.Product;
import com.app.payloads.ProductDTO;

public interface ProductService {
	
	ProductDTO addProduct(Integer categoryId, Product product);
	
	List<ProductDTO> searchByCategory(Integer categoryId);
	
	ProductDTO updateProduct(Integer categoryId, Integer productId, Product product);
	
	String deleteProduct(Integer categoryId, Integer productId);
}