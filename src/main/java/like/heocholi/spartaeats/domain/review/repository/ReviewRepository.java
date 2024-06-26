package like.heocholi.spartaeats.domain.review.repository;

import like.heocholi.spartaeats.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    Optional<Review> findByStoreIdAndId(Long reviewId, Long storeId);
}
