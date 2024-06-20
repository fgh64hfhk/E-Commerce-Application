package com.app.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDTO {

	private Long productVariantId;
	private String color;
	private String size;
	private Integer inventory;
	private String sku;
	private String image;

}
