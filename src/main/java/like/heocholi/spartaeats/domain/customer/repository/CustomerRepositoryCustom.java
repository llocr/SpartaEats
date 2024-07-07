package like.heocholi.spartaeats.domain.customer.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepositoryCustom {
	long findLikeReviewCount(Long userId);
}