package like.heocholi.spartaeats.domain.pick.exception;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.global.exception.CustomException;

public class PickException extends CustomException {

    public PickException(ErrorType errorType) {
        super(errorType);
    }

}
