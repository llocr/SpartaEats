package like.heocholi.spartaeats.domain.customer.exception;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.global.exception.CustomException;

public class CustomerException extends CustomException {
    public CustomerException(ErrorType errorType) {
        super(errorType);
    }
}
