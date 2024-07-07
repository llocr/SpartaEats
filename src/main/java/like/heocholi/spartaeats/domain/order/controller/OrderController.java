package like.heocholi.spartaeats.domain.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import like.heocholi.spartaeats.domain.order.dto.OrderListResponseDTO;
import like.heocholi.spartaeats.domain.order.dto.OrderResponseDTO;
import like.heocholi.spartaeats.domain.common.dto.ResponseMessage;
import like.heocholi.spartaeats.global.security.UserDetailsImpl;
import like.heocholi.spartaeats.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
	private final OrderService orderService;
	
	/**
	 * 주문하기
	 * @param userDetails
	 * @return ResponseEntity<ResponseMessage<OrderResponseDTO>>
	 *     - statusCode: 201
	 *     - message: "주문이 완료되었습니다."
	 *     - data: 주문 정보
	 */
	@PostMapping
	public ResponseEntity<ResponseMessage<OrderResponseDTO>> addOrder(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		OrderResponseDTO responseDTO = orderService.saveOrder(userDetails.getCustomer());
		
		ResponseMessage<OrderResponseDTO> responseMessage = ResponseMessage.<OrderResponseDTO>builder()
			.statusCode(HttpStatus.CREATED.value())
			.message("주문이 완료되었습니다.")
			.data(responseDTO)
			.build();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
	}
	
	/**
	 * 주문 목록 불러오기
	 * @param page
	 * @param userDetails
	 * @return ResponseEntity<ResponseMessage<OrderListResponseDTO>>
	 *     - statusCode: 200
	 *     - message: "주문 목록을 불러왔습니다."
	 *     - data: 주문 목록
	 */
	@GetMapping
	public ResponseEntity<ResponseMessage<OrderListResponseDTO>> getOrder(
		@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		OrderListResponseDTO responseDTO = orderService.getOrders(page, userDetails.getCustomer());
		
		ResponseMessage<OrderListResponseDTO> responseMessage = ResponseMessage.<OrderListResponseDTO>builder()
			.statusCode(HttpStatus.OK.value())
			.message("주문 목록을 불러왔습니다.")
			.data(responseDTO)
			.build();
		
		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}
	
	/**
	 * 주문 상세 정보 불러오기
	 * @param id
	 * @param userDetails
	 * @return ResponseEntity<ResponseMessage<OrderResponseDTO>>
	 *     - statusCode: 200
	 *     - message: "주문 상세 정보를 불러왔습니다."
	 *     - data: 주문 상세 정보
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ResponseMessage<OrderResponseDTO>> getOrderDetail(@PathVariable Long id,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		OrderResponseDTO responseDTO = orderService.getOrderDetails(id, userDetails.getCustomer());
		
		ResponseMessage<OrderResponseDTO> responseMessage = ResponseMessage.<OrderResponseDTO>builder()
			.statusCode(HttpStatus.OK.value())
			.message("주문 상세 정보를 불러왔습니다.")
			.data(responseDTO)
			.build();
		
		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}
	
	/**
	 * 주문 취소하기
	 * @param id
	 * @param userDetails
	 * @return ResponseEntity<ResponseMessage<Long>>
	 *     - statusCode: 200
	 *     - message: "주문이 취소되었습니다."
	 *     - data: 주문 ID
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ResponseMessage<Long>> cancelOrder(@PathVariable Long id,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Long responseId = orderService.cancelOrder(id, userDetails.getCustomer());
		
		ResponseMessage<Long> responseMessage = ResponseMessage.<Long>builder()
			.statusCode(HttpStatus.OK.value())
			.message("주문이 취소되었습니다.")
			.data(responseId)
			.build();
		
		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}
}
