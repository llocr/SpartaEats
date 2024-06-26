package like.heocholi.spartaeats.domain.cart.exception;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.global.exception.CustomException;

public class CartException extends CustomException {
	public CartException(ErrorType errorType) {
		super(errorType);
	}
}
