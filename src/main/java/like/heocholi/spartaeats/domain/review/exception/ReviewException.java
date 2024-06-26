package like.heocholi.spartaeats.domain.review.exception;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.global.exception.CustomException;

public class ReviewException extends CustomException {
    public ReviewException(ErrorType errorType) {
        super(errorType);
    }
}