package like.heocholi.spartaeats.domain.like.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.like.entity.Like;
import like.heocholi.spartaeats.domain.like.exception.LikeException;
import like.heocholi.spartaeats.domain.like.repository.LikeRepository;
import like.heocholi.spartaeats.domain.review.entity.Review;
import like.heocholi.spartaeats.domain.review.service.ReviewService;
import like.heocholi.spartaeats.global.exception.ErrorType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final ReviewService reviewService;

    /**
     * 리뷰 좋아요 토글
     * @param reviewId
     * @param customer
     * @return 좋아요 여부
     */
    @Transactional
    public boolean likeReview(Long reviewId, Customer customer) {
        Review review = reviewService.findReviewById(reviewId);

        //본인이 작성한 리뷰에는 좋아요를 남길 수 없다.
        if (customer.getId().equals(review.getCustomer().getId())) {
            throw new LikeException(ErrorType.INVALID_LIKE);
        }

        boolean result = toggleLike(customer, review);
        review.updateLike(result);

        return result;
    }

    /**
     * 좋아요 토글
     * @param customer
     * @param review
     * @return 좋아요 여부
     */
    private boolean toggleLike(Customer customer, Review review) {
        Optional<Like> optionalLike = likeRepository.findByCustomerIdAndReviewId(customer.getId(), review.getId());
        Like like;

        if(optionalLike.isPresent()) {
            like = optionalLike.get();
            like.update();
        } else {
            like  = Like.builder()
                    .customer(customer)
                    .review(review)
                    .isLike(true)
                    .build();

            likeRepository.save(like);
        }

        return like.isLike();
    }
}
