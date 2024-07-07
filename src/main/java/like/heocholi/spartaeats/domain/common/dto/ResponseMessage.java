package like.heocholi.spartaeats.domain.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseMessage<T> {
	private Integer statusCode;
	private String message;
	private T data;
}
