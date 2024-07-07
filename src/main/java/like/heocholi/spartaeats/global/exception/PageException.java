package like.heocholi.spartaeats.global.exception;

public class PageException extends CustomException {
	public PageException(ErrorType errorType) {
		super(errorType);
	}
}
