package like.heocholi.spartaeats.domain.menu.exception;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.global.exception.CustomException;

public class MenuException  extends CustomException {
    public MenuException(ErrorType message) {
        super(message);
    }
}
