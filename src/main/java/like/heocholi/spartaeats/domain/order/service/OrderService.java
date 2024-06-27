package like.heocholi.spartaeats.domain.order.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.domain.cart.service.CartService;
import like.heocholi.spartaeats.domain.order.dto.OrderListResponseDTO;
import like.heocholi.spartaeats.domain.order.dto.OrderResponseDTO;
import like.heocholi.spartaeats.domain.cart.entity.Cart;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.order.entity.Order;
import like.heocholi.spartaeats.domain.order.entity.OrderMenu;
import like.heocholi.spartaeats.domain.store.entity.Store;
import like.heocholi.spartaeats.domain.order.exception.OrderException;
import like.heocholi.spartaeats.global.exception.PageException;
import like.heocholi.spartaeats.domain.order.repository.OrderMenuRepository;
import like.heocholi.spartaeats.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final CartService cartService;
	private final OrderMenuRepository orderMenuRepository;
	
	/**
	 * 주문하기
	 * @param customer
	 * @return 주문 정보
	 */
	@Transactional
	public OrderResponseDTO saveOrder(Customer customer) {
		List<Cart> cartList = cartService.getCartList(customer);
		
		if (cartList.isEmpty()) {
			throw new OrderException(ErrorType.NOT_FOUND_CART);
		}
		
		Store store = cartList.get(0).getStore();
		int totalPrice = cartList.stream().mapToInt(cart -> cart.getMenu().getPrice() * cart.getQuantity()).sum();
		
		Order order = new Order(store, customer);
		Order saveOrder = orderRepository.save(order);
		
		List<OrderMenu> orderMenuList = cartList.stream()
			.map(cart -> OrderMenu.builder()
				.order(saveOrder)
				.menu(cart.getMenu())
				.count(cart.getQuantity())
				.price(cart.getMenu().getPrice() * cart.getQuantity())
				.build())
			.toList();
		
		orderMenuRepository.saveAll(orderMenuList);
		
		saveOrder.updateOrder(orderMenuList, totalPrice);
		
		cartService.deleteAllCart(customer);
		
		return new OrderResponseDTO(saveOrder);
	}
	
	/**
	 * 주문 목록 조회
	 * @param page
	 * @param customer
	 * @return 주문 목록
	 */
	public OrderListResponseDTO getOrders(Integer page, Customer customer) {
		Pageable pageable = createPageable(page);
		Page<Order> orderPage = orderRepository.findAllByCustomer(customer, pageable);
		
		checkValidatePage(page, orderPage);
		
		return new OrderListResponseDTO(page, orderPage);
	}
	
	
	/**
	 * 주문 상세 정보 조회
	 * @param orderId
	 * @param customer
	 * @return 주문 정보
	 */
	public OrderResponseDTO getOrderDetails(Long orderId, Customer customer) {
		Order order = findOrderById(orderId);
		checkValidateUser(order, customer);
		
		return new OrderResponseDTO(order);
	}
	
	/**
	 * 주문 취소
	 * @param orderId
	 * @param customer
	 * @return 주문 ID
	 */
	@Transactional
	public Long cancelOrder(Long orderId, Customer customer) {
		Order order = findOrderById(orderId);
		checkValidateUser(order, customer);
		
		order.cancelOrder();
		
		return orderId;
	}
	
	/* util */
	
	/**
	 * 주문 조회
	 * @param orderId
	 * @return 주문
	 */
	public Order findOrderById(Long orderId) {
		return orderRepository.findById(orderId).orElseThrow(() -> new OrderException(ErrorType.NOT_FOUND_ORDER));
	}
	
	/**
	 * 주문 유효성 검사
	 * @param order
	 * @param customer
	 */
	private void checkValidateUser(Order order, Customer customer) {
		if (!order.getCustomer().getId().equals(customer.getId())) {
			throw new OrderException(ErrorType.INVALID_ORDER_CUSTOMER);
		}
	}
	
	/**
	 * 페이지 생성
	 * @param page
	 * @return 페이지 정보
	 */
	private Pageable createPageable(Integer page) {
		return PageRequest.of(page-1, 5, Sort.by("createdAt").descending());
	}
	
	/**
	 * 페이지 유효성 검사
	 * @param page
	 * @param orderPage
	 */
	private static void checkValidatePage(Integer page, Page<Order> orderPage) {
		if (orderPage.getTotalElements() == 0) {
			throw new OrderException(ErrorType.NOT_FOUND_ORDER);
		}
		
		if (page > orderPage.getTotalPages() || page < 1) {
			throw new PageException(ErrorType.INVALID_PAGE);
		}
	}
}
