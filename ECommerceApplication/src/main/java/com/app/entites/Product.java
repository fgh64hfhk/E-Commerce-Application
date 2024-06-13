package com.app.entites;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long productId;

	@NotBlank
	@Size(min = 3, message = "Product name must contain atleast 3 characters")
	private String productName;

	private String image;

	@NotBlank
//	@Size(min = 3, message = "Product description must contain atleast 6 characters")
	private String description;

	private Integer quantity;
	private double price;
	private double discount;
	private double specialPrice;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "product", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private List<ProductVariant> variants = new ArrayList<>();

	@OneToMany(mappedBy = "product", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	private List<CartItem> products = new ArrayList<>();

	@OneToMany(mappedBy = "product", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<OrderItem> orderItems = new ArrayList<>();

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", image=" + image
				+ ", description=" + description + ", quantity=" + quantity + ", price=" + price + ", discount="
				+ discount + ", specialPrice=" + specialPrice + "]";
	}

}
