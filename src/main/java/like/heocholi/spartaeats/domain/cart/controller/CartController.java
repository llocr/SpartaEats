package like.heocholi.spartaeats.domain.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import like.heocholi.spartaeats.domain.cart.dto.CartRequestDTO;
import like.heocholi.spartaeats.domain.cart.dto.CartResponseDTO;
import like.heocholi.spartaeats.domain.common.dto.ResponseMessage;
import like.heocholi.spartaeats.global.security.UserDetailsImpl;
import like.heocholi.spartaeats.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
	private final CartService cartService;
	
	// 장바구니에 메뉴 추가
	@PostMapping
	public ResponseEntity<ResponseMessage<Long>> addCart(@Valid @RequestBody CartRequestDTO cartRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		Long menuId = cartService.addCart(cartRequestDTO, userDetails.getCustomer());
		
		ResponseMessage<Long> responseMessage = ResponseMessage.<Long>builder()
			.statusCode(HttpStatus.CREATED.value())
			.message("장바구니에 메뉴가 추가되었습니다.")
			.data(menuId)
			.build();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
	}
	
	// 장바구니 목록 불러오기
	@GetMapping
	public ResponseEntity<ResponseMessage<CartResponseDTO>> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		CartResponseDTO cartResponseDTO = cartService.getCarts(userDetails.getCustomer());
		
		ResponseMessage<CartResponseDTO> responseMessage = ResponseMessage.<CartResponseDTO>builder()
			.statusCode(HttpStatus.OK.value())
			.message("장바구니 목록을 불러왔습니다.")
			.data(cartResponseDTO)
			.build();
		
		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}
	
	// 장바구니 전체 삭제
	@DeleteMapping
	public ResponseEntity<ResponseMessage<Long>> deleteAllCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Long customerId = cartService.deleteAllCart(userDetails.getCustomer());
		
		ResponseMessage<Long> responseMessage = ResponseMessage.<Long>builder()
			.statusCode(HttpStatus.OK.value())
			.message("장바구니가 비워졌습니다.")
			.data(customerId)
			.build();
		
		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}
	
	// 장바구니 단일 삭제
	@DeleteMapping("/{menuId}")
	public ResponseEntity<ResponseMessage<Long>> deleteCart(@PathVariable Long menuId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		Long deleteMenuId = cartService.deleteCart(menuId, userDetails.getCustomer());
		
		ResponseMessage<Long> responseMessage = ResponseMessage.<Long>builder()
			.statusCode(HttpStatus.OK.value())
			.message("장바구니에서 메뉴가 삭제되었습니다.")
			.data(deleteMenuId)
			.build();
		
		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}
	
}
