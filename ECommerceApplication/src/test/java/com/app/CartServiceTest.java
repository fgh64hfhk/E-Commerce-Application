package com.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.app.entites.CartItem;
import com.app.entites.ProductVariant;
import com.app.repositories.CartItemRepo;
import com.app.services.CartService;
import com.app.services.CartServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CartServiceTest {

	@InjectMocks
	private CartServiceImpl cartService;

	@Mock
	private CartItemRepo cartItemRepo;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testFindCartItemBySku() {
		// 準備數據
		String sku = "";
		ProductVariant productVariant = new ProductVariant();
		productVariant.setSku(sku);

		CartItem cartItem = new CartItem();
		cartItem.setProductVariant(productVariant);

		// 創建 Example 和 ExampleMatcher
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("productVariant.sku",
				ExampleMatcher.GenericPropertyMatchers.exact());
		Example<CartItem> example = Example.of(cartItem, matcher);

		// 設置模擬行為
		when(cartItemRepo.findAll(example)).thenReturn(Collections.singletonList(cartItem));

		// 執行測試
		List<CartItem> result = cartService.findCartItemsBySku(sku);

		// 驗證結果
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		assertEquals(sku, result.get(0).getProductVariant().getSku());

		// 驗證方法的調用
		verify(cartItemRepo, times(1)).findAll(example);
	}
}
