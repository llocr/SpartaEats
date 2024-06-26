package like.heocholi.spartaeats.domain.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequestDTO {
	@NotNull(message = "메뉴 아이디를 입력해주세요.")
	private Long menuId;
	
	@NotNull(message = "수량을 입력해주세요.")
	@Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
	private int quantity;
}
