package com.jf.electronicstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jf.electronicstore.cart.application.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CartIT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void add_discounted_items_to_cart_and_get_invoice() throws Exception {
		ProductDTO product_1 = new ProductDTO(1L, "Apple", new BigDecimal("1.10"), 3, new ArrayList<>());
		ProductDTO product_2 = new ProductDTO(2L, "Avocado", new BigDecimal("2.20"), 5, new ArrayList<>());
		ProductDTO product_3 = new ProductDTO(3L, "myProduct", new BigDecimal("100.00"), 2, new ArrayList<>());

		CouponDTO coupon_1 = new CouponDTO(1L, 2, 100); // buy 1, get 1 free
		CouponDTO coupon_2 = new CouponDTO(2L, 5, 50); // buy 5, get 1 -50%
		CouponDTO coupon_3 = new CouponDTO(3L, 1, 10); // -10%

		CartDTO cart_1 = new CartDTO(1L , new ArrayList<>());
		CartItemDTO cart_item_1 = new CartItemDTO(1L, 1L, 2);
		CartItemDTO cart_item_2 = new CartItemDTO(2L, 2L, 3);
		CartItemDTO cart_item_3 = new CartItemDTO(3L, 3L, 1);

		addProduct(product_1);
		addProduct(product_2);
		addProduct(product_3);
		getProducts(List.of(product_1, product_2, product_3));

		addCoupon(coupon_1, product_1);
		addCoupon(coupon_2, product_1);
		addCoupon(coupon_3, product_2);
		getCoupons(product_1.getId(), List.of(coupon_1, coupon_2));

		addCart(cart_1);

		addCartItem(cart_item_1, cart_1);
		addCartItem(cart_item_2, cart_1);
		addCartItem(cart_item_3, cart_1);

		getCartItems(cart_1.id(), List.of(cart_item_1, cart_item_2, cart_item_3));
		deleteCartItem(cart_1.id(), cart_item_3.id());
		getCartItems(cart_1.id(), List.of(cart_item_1, cart_item_2));

		BigDecimal expectedPrice = new BigDecimal("8.80"); // (2 * 1.10) + (3 * 2.20)
		BigDecimal expectedDiscount = new BigDecimal("1.76"); // (1 * 1.10 * 100%) + (3 * 2.20 * 10%)
		List<CartItemDTO> expectedItems = List.of(cart_item_1, cart_item_2);
		Map<Long, List<CouponDTO>> expectedDiscountApplied = Map.of(1L, List.of(coupon_1), 2L, List.of(coupon_3));
		InvoiceDTO expectedInvoice = new InvoiceDTO(expectedPrice, expectedDiscount, expectedItems, expectedDiscountApplied);

		getInvoice(1L, expectedInvoice);

		// Delete a product and expect that the invoice doesn't include it anymore
		deleteProduct(1L);

		expectedPrice = new BigDecimal("6.60"); // (3 * 2.20)
		expectedDiscount = new BigDecimal("0.66"); // (3 * 2.20 * 10%)
		expectedDiscountApplied = Map.of(2L, List.of(coupon_3));
		expectedInvoice = new InvoiceDTO(expectedPrice, expectedDiscount, List.of(cart_item_2), expectedDiscountApplied);
		getInvoice(1L, expectedInvoice);
	}

	private void addCart(CartDTO cart) throws Exception {
		doRequest(HttpMethod.POST, "/carts", "")
				.andExpect(status().isCreated())
				.andExpect(header().string("location", String.format("/carts/%d", cart.id())))
				.andExpect(content().json(toJson(cart)));
	}

	private void addProduct(ProductDTO product) throws Exception {
		doRequest(HttpMethod.POST, "/products", toJson(product))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", String.format("/products/%d", product.getId())))
				.andExpect(content().json(toJson(product)));
	}

	private void getProducts(List<ProductDTO> expectedBody) throws Exception {
		doRequest(HttpMethod.GET, "/products", "")
				.andExpect(status().isOk())
				.andExpect(content().json(toJson(expectedBody)));
	}

	private void deleteProduct(Long productId) throws Exception {
		String path = String.format("/products/%d", productId);
		doRequest(HttpMethod.DELETE, path, "")
				.andExpect(status().isNoContent());
	}

	private void addCoupon(CouponDTO coupon, ProductDTO expectedProduct) throws Exception {
		expectedProduct.getCoupons().add(coupon);

		String path = String.format("/products/%d/coupons", expectedProduct.getId());
		doRequest(HttpMethod.POST, path, toJson(coupon))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", String.format("/products/%d", expectedProduct.getId())))
				.andExpect(content().json(toJson(expectedProduct)));
	}

	private void getCoupons(Long productId, List<CouponDTO> expectedBody) throws Exception {
		String path = String.format("/products/%d/coupons", productId);
		doRequest(HttpMethod.GET, path, "")
				.andExpect(status().isOk())
				.andExpect(content().json(toJson(expectedBody)));
	}

	private void addCartItem(CartItemDTO cartItem, CartDTO expectedCart) throws Exception {
		expectedCart.items().add(cartItem);

		String path = String.format("/carts/%d/items", expectedCart.id());
		doRequest(HttpMethod.POST, path, toJson(cartItem))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", String.format("/carts/%d", expectedCart.id())))
				.andExpect(content().json(toJson(expectedCart)));
	}

	private void deleteCartItem(Long cartId, Long cartItemId) throws Exception {
		String path = String.format("/carts/%d/items/%d", cartId, cartItemId);
		doRequest(HttpMethod.DELETE, path, "")
				.andExpect(status().isNoContent());
	}

	private void getInvoice(Long cartId, InvoiceDTO expectedBody) throws Exception {
		String path = String.format("/carts/%d/invoice", cartId);
		doRequest(HttpMethod.GET, path, "")
				.andExpect(status().isOk())
				.andExpect(content().json(toJson(expectedBody)));
	}

	private void getCartItems(Long cartId, List<CartItemDTO> expectedCartItems) throws Exception {
		String path = String.format("/carts/%d/items", cartId);
		doRequest(HttpMethod.GET, path, "")
				.andExpect(status().isOk())
				.andExpect(content().json(toJson(expectedCartItems)));
	}

	private ResultActions doRequest(HttpMethod method, String path, String content) throws Exception {
		return this.mockMvc.perform(request(method, path)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(content));
	}

	private static String toJson(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}

}