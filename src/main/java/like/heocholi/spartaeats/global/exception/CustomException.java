package like.heocholi.spartaeats.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public abstract class CustomException extends RuntimeException {
    private ErrorType errorType;
}
