package like.heocholi.spartaeats.domain.store.exception;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.global.exception.CustomException;

public class StoreException extends CustomException {
    public StoreException(ErrorType errorType) {
        super(errorType);
    }
}