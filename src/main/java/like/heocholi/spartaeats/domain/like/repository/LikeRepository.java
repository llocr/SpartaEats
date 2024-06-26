package like.heocholi.spartaeats.domain.like.repository;

import like.heocholi.spartaeats.domain.like.entity.Like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByCustomerIdAndReviewId(Long customerId, Long reviewId);
}
