package like.heocholi.spartaeats.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSearchCond {
	private String storeName;
	private String address;
	private String restaurantType;
}
