package like.heocholi.spartaeats.domain.customer.exception;

import like.heocholi.spartaeats.global.exception.ErrorType;

public class PasswordException extends CustomerException {
	public PasswordException(ErrorType errorType) {
		super(errorType);
	}
}
