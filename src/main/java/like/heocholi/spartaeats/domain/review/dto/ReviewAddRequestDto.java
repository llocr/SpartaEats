package like.heocholi.spartaeats.domain.review.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReviewAddRequestDto {

    @NotBlank(message = "내용을 입력해주세요")
    private String contents;

}
