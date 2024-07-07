package like.heocholi.spartaeats.domain.cart.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartResponseDTO {
	private String storeName;
	private List<String> menuNames;
	private int totalPrice;
}
