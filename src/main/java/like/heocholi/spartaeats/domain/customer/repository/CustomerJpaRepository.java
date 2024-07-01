package like.heocholi.spartaeats.domain.customer.repository;

import static like.heocholi.spartaeats.domain.like.entity.QLike.like;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class CustomerJpaRepository {
	private final JPAQueryFactory queryFactory;
	
	public CustomerJpaRepository(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	// 좋아요 누른 리뷰 수 조회
	public long findLikeReviewCount(Long userId) {
		return queryFactory
			.select(like)
			.from(like)
			.where(like.isLike.isTrue().and(like.customer.id.eq(userId)))
			.fetchCount();
	}
	
	
}
