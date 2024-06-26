package like.heocholi.spartaeats.domain.cart.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.domain.cart.dto.CartRequestDTO;
import like.heocholi.spartaeats.domain.cart.dto.CartResponseDTO;
import like.heocholi.spartaeats.domain.cart.entity.Cart;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.menu.entity.Menu;
import like.heocholi.spartaeats.domain.cart.exception.CartException;
import like.heocholi.spartaeats.domain.cart.repository.CartRepository;
import like.heocholi.spartaeats.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {
	private final CartRepository cartRepository;
	private final MenuRepository menuRepository;
	
	/*
	 * 1. 장바구니에 메뉴 추가
	 */
	@Transactional
	public Long addCart(CartRequestDTO cartRequestDTO, Customer customer) {
		Long menuId = cartRequestDTO.getMenuId();
		
		Menu menu = getMenu(menuId);
		List<Cart> cartList = getCartList(customer);
		
		for (Cart cart : cartList) {
			if (!Objects.equals(menu.getStore().getId(), cart.getStore().getId())) {
				throw new CartException(ErrorType.INVALID_CART);
			}
			
			if (Objects.equals(menuId, cart.getMenu().getId())) {
				throw new CartException(ErrorType.DUPLICATE_CART);
			}
		}
		
		Cart cart = Cart.builder()
			.menu(menu)
			.store(menu.getStore())
			.customer(customer)
			.quantity(cartRequestDTO.getQuantity())
			.build();
		
		cartRepository.save(cart);
		
		return menuId;
	}
	
	/*
	 * 2. 장바구니 목록 불러오기
	 */
	public CartResponseDTO getCarts(Customer customer) {
		List<Cart> cartList = getCartList(customer);
		
		if (cartList.isEmpty()) {
			throw new CartException(ErrorType.NOT_FOUND_CART);
		}
		
		String storeName = cartList.get(0).getStore().getName();
		List<String> menuNames = cartList.stream().map(cart -> cart.getMenu().getName()).toList();
		int totalPrice = cartList.stream().mapToInt(cart -> cart.getMenu().getPrice()).sum();
		
		return new CartResponseDTO(storeName, menuNames, totalPrice);
	}
	
	/*
	 * 3. 장바구니 전체 삭제
	 */
	@Transactional
	public Long deleteAllCart(Customer customer) {
		List<Cart> cartList = getCartList(customer);
		cartRepository.deleteAll(cartList);
		
		return customer.getId();
	}
	
	/*
	 * 4. 장바구니 단일 삭제
	 */
	@Transactional
	public Long deleteCart(Long menuId, Customer customer) {
		Menu menu = getMenu(menuId);
		Cart cart = cartRepository.findByMenuAndCustomer(menu, customer)
			.orElseThrow(() -> new CartException(ErrorType.NOT_FOUND_CART_MENU));
		
		cartRepository.delete(cart);
		
		return menuId;
	}
	
	/*
	 * 메뉴 조회
	 */
	private Menu getMenu(Long menuId) {
		return menuRepository.findById(menuId).orElseThrow(() -> new CartException(ErrorType.NOT_FOUND_MENU));
	}
	
	/*
	 * 장바구니 조회
	 */
	public List<Cart> getCartList(Customer customer) {
		return cartRepository.findByCustomer(customer);
	}
}
