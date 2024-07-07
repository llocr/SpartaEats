package like.heocholi.spartaeats.domain.like.controller;

import like.heocholi.spartaeats.domain.common.dto.ResponseMessage;
import like.heocholi.spartaeats.global.security.UserDetailsImpl;
import like.heocholi.spartaeats.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class LikeController {
    private final LikeService likeService;

    /**
     * 리뷰 좋아요 등록/취소
     * @param reviewId
     * @param userDetails
     * @return ResponseEntity<ResponseMessage<Long>>
     *     - statusCode: 200
     *     - message: "좋아요 등록" or "좋아요 취소"
     *     - data: 리뷰Id
     */
    @PostMapping("/{reviewId}/like")
    public ResponseEntity<ResponseMessage<Long>> likeReview(@PathVariable Long reviewId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        boolean check = likeService.likeReview(reviewId, userDetails.getCustomer());
        String message = check ? "좋아요 등록" : "좋아요 취소";

        ResponseMessage<Long> responseMessage = ResponseMessage.<Long>builder()
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .data(reviewId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
