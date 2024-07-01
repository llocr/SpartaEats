package like.heocholi.spartaeats.domain.review.controller;

import jakarta.validation.Valid;
import like.heocholi.spartaeats.domain.common.dto.ResponseMessage;
import like.heocholi.spartaeats.domain.review.dto.ReviewAddRequestDto;
import like.heocholi.spartaeats.domain.review.dto.ReviewListResponseDTO;
import like.heocholi.spartaeats.domain.review.dto.ReviewResponseDto;
import like.heocholi.spartaeats.domain.review.dto.ReviewUpdateRequestDto;
import like.heocholi.spartaeats.global.security.UserDetailsImpl;
import like.heocholi.spartaeats.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 전체 조회
     * @param storeId
     * @return ResponseEntity<ResponseMessage<List<ReviewResponseDto>>>
     *     - statusCode: 200
     *     - message: "모든 리뷰가 조회되었습니다."
     *     - data: 리뷰 리스트
     */
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<ResponseMessage<List<ReviewResponseDto>>> getReview(@PathVariable Long storeId) {
        List<ReviewResponseDto> reviewList = reviewService.getReviews(storeId);

        ResponseMessage<List<ReviewResponseDto>> responseMessage = ResponseMessage.<List<ReviewResponseDto>>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("모든 리뷰가 조회되었습니다.")
                .data(reviewList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 리뷰 단건 조회
     * @param storeId
     * @param reviewId
     * @return ResponseEntity<ResponseMessage<ReviewResponseDto>>
     *     - statusCode: 200
     *     - message: "해당 리뷰가 조회되었습니다."
     *     - data: 리뷰 정보
     */
    @GetMapping("/stores/{storeId}/reviews/{reviewId}")
    public ResponseEntity<ResponseMessage<ReviewResponseDto>> getReview(@PathVariable Long storeId,
                                                                        @PathVariable Long reviewId) {
        ReviewResponseDto responseDto = reviewService.getReview(storeId, reviewId);

        ResponseMessage<ReviewResponseDto> responseMessage = ResponseMessage.<ReviewResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message("해당 리뷰가 조회되었습니다.")
                .data(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 리뷰 작성
     * @param orderId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<ResponseMessage<ReviewResponseDto>>
     *     - statusCode: 201
     *     - message: "리뷰가 등록되었습니다."
     *     - data: 리뷰 정보
     */
    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ResponseMessage<ReviewResponseDto>> addReview(@PathVariable Long orderId,
                                                                        @RequestBody @Valid ReviewAddRequestDto requestDto,
                                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReviewResponseDto responseDto = reviewService.addReview(orderId, requestDto, userDetails.getCustomer());

        ResponseMessage<ReviewResponseDto> responseMessage = ResponseMessage.<ReviewResponseDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("리뷰가 등록되었습니다.")
                .data(responseDto)
                .build();


        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    /**
     * 리뷰 수정
     * @param reviewId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<ResponseMessage<ReviewResponseDto>>
     *     - statusCode: 200
     *     - message: "리뷰가 수정 되었습니다."
     *     - data: 리뷰 정보
     */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseMessage<ReviewResponseDto>> updateReview(@PathVariable Long reviewId,
                                                                            @RequestBody @Valid ReviewUpdateRequestDto requestDto,
                                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto responseDto = reviewService.updateReview(reviewId, requestDto, userDetails.getCustomer());

        ResponseMessage<ReviewResponseDto> responseMessage = ResponseMessage.<ReviewResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message("리뷰가 수정 되었습니다.")
                .data(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }


    /**
     * 리뷰 삭제
     * @param reviewId
     * @param userDetails
     * @return ResponseEntity<ResponseMessage<Long>>
     *     - statusCode: 200
     *     - message: "리뷰가 삭제 되었습니다."
     *     - data: 리뷰 ID
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseMessage<Long>> deleteReview(@PathVariable Long reviewId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long deleteId = reviewService.deleteReview(reviewId, userDetails.getCustomer());

        ResponseMessage<Long> responseMessage = ResponseMessage.<Long>builder()
                .statusCode(HttpStatus.OK.value())
                .message("리뷰가 삭제 되었습니다.")
                .data(deleteId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
    
    @GetMapping("/reviews/likes")
    public ResponseEntity<ResponseMessage<ReviewListResponseDTO>> likeReviews(
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewListResponseDTO responseDto = reviewService.likeReviews(page, userDetails.getCustomer());
        
        ResponseMessage<ReviewListResponseDTO> responseMessage = ResponseMessage.<ReviewListResponseDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("좋아요한 리뷰가 조회되었습니다.")
                .data(responseDto)
                .build();
        
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
