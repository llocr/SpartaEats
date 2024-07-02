package like.heocholi.spartaeats.domain.customer.repository;

import static like.heocholi.spartaeats.domain.like.entity.QLike.like;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class CustomerJpaRepository implements CustomerRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	
	public CustomerJpaRepository(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	/**
	 * 좋아요한 리뷰 개수 조회
	 * @param userId 사용자 ID
	 * @return long
	 */
	public long findLikeReviewCount(Long userId) {
		return queryFactory
			.select(like)
			.from(like)
			.where(like.isLike.isTrue().and(like.customer.id.eq(userId)))
			.fetchCount();
	}
	
	
}
