package like.heocholi.spartaeats.domain.review.repository;

import like.heocholi.spartaeats.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long>, ReviewRepositoryCustom {

    Optional<Review> findByStoreIdAndId(Long reviewId, Long storeId);
}
