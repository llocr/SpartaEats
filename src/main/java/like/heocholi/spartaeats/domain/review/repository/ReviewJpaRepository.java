package like.heocholi.spartaeats.domain.review.repository;

import static like.heocholi.spartaeats.domain.like.entity.QLike.like;
import static like.heocholi.spartaeats.domain.review.entity.QReview.review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import like.heocholi.spartaeats.domain.review.entity.Review;

@Repository
public class ReviewJpaRepository {
	private final JPAQueryFactory queryFactory;
	
	public ReviewJpaRepository(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	// 좋아요 한 리뷰 조회
	public Page<Review> findLikeReview(Long userId, Pageable pageable) {
		// 페이징 및 정렬
		OrderSpecifier<?> sortedColumn = review.createdAt.desc();  // 기본값 설정
		
		// 좋아요한 리뷰 조회 쿼리
		List<Review> content = queryFactory
			.selectFrom(review)
			.join(like).on(like.review.id.eq(review.id))
			.where(like.isLike.isTrue().and(like.customer.id.eq(userId)))
			.orderBy(sortedColumn)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		
		// 총 개수 조회 쿼리
		JPAQuery<Review> countQuery = queryFactory
			.selectFrom(review)
			.join(like).on(like.review.id.eq(review.id))
			.where(like.isLike.isTrue().and(like.customer.id.eq(userId)));
		
		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
	}
}
