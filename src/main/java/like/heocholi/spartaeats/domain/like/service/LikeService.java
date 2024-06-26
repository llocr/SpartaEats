package like.heocholi.spartaeats.domain.like.service;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.like.entity.Like;
import like.heocholi.spartaeats.domain.review.entity.Review;
import like.heocholi.spartaeats.domain.like.exception.LikeException;
import like.heocholi.spartaeats.domain.like.repository.LikeRepository;
import like.heocholi.spartaeats.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;

    //고객이 특정 리뷰에 좋아요를 등록하는 메서드.
    @Transactional
    public boolean likeReview(Long reviewId, Customer customer) {
        Review review = findReviewById(reviewId);

        //본인이 작성한 리뷰에는 좋아요를 남길 수 없다.
        if (customer.getId().equals(review.getCustomer().getId())) {
            throw new LikeException(ErrorType.INVALID_LIKE);
        }

        boolean result = toggleLike(customer, review);
        review.updateLike(result);

        return result;
    }

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

    //리뷰 ID를 기준으로 Review 엔티티를 조회하는 메서드.
    private Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new LikeException(ErrorType.NOT_FOUND_REVIEW));
    }
}
