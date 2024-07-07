package like.heocholi.spartaeats.domain.like.exception;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.domain.customer.exception.CustomerException;

public class LikeException extends CustomerException {
    public LikeException(ErrorType errorType) {
        super(errorType);
    }
}