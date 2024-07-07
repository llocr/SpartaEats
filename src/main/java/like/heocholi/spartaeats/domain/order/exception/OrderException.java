package like.heocholi.spartaeats.domain.order.exception;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.global.exception.CustomException;

public class OrderException extends CustomException {
	public OrderException(ErrorType errorType) {
		super(errorType);
	}
}
