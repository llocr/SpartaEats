package like.heocholi.spartaeats.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import like.heocholi.spartaeats.domain.review.entity.Review;

public interface ReviewRepositoryCustom {
	Page<Review> findLikeReview(Long userId, Pageable pageable);
	
	Page<Review> findPickReview(Long userId, Pageable pageable);
}
