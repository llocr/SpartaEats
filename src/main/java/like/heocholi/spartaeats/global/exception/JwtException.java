package like.heocholi.spartaeats.global.exception;

import lombok.Getter;


@Getter
public class JwtException extends CustomException{

    public JwtException(ErrorType errorType) {
        super(errorType);
    }
}
