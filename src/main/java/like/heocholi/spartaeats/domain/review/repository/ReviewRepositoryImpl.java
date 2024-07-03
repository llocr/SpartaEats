package like.heocholi.spartaeats.domain.review.repository;

import static like.heocholi.spartaeats.domain.like.entity.QLike.like;
import static like.heocholi.spartaeats.domain.pick.entity.QPick.pick;
import static like.heocholi.spartaeats.domain.review.entity.QReview.review;
import static like.heocholi.spartaeats.domain.store.entity.QStore.store;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import like.heocholi.spartaeats.domain.review.dto.ReviewSearchCond;
import like.heocholi.spartaeats.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	
	/**
	 * 좋아요한 리뷰 목록 조회
	 * @param userId 사용자 ID
	 * @param pageable 페이징 정보
	 * @return Page<Review>
	 */
	@Override
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
	
	/**
	 * 찜한 가게의 리뷰 목록 조회
	 * @param userId 사용자 ID
	 * @param pageable 페이징 정보
	 * @return Page<Review>
	 */
	@Override
	public Page<Review> findPickReview(ReviewSearchCond cond, Long userId, Pageable pageable) {
		// 기본 정렬 조건 설정
		OrderSpecifier<?> sortedColumn = review.createdAt.desc();
		
		// 정렬 조건 확인 및 설정
		for (Sort.Order order : pageable.getSort()) {
			if (order.getProperty().equals("userId")) {
				sortedColumn = review.customer.id.asc();
			} else if (order.getProperty().equals("name")) {
				sortedColumn = review.store.name.asc();
			}
		}
		
		BooleanBuilder builder = getBooleanBuilder(cond);
		
		List<Review> content = queryFactory
			.selectFrom(review)
			.join(review.store, store)
			.join(pick).on(pick.store.id.eq(store.id))
			.where(pick.isPick.isTrue()
				.and(pick.customer.id.eq(userId))
				.and(builder))
			.orderBy(sortedColumn)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		
		JPAQuery<Review> countQuery = queryFactory
			.selectFrom(review)
			.join(review.store, store)
			.join(pick).on(pick.store.id.eq(store.id))
			.where(pick.isPick.isTrue().and(pick.customer.id.eq(userId)).and(builder));
		 
		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
	}
	
	/**
	 * 검색 조건에 따른 BooleanBuilder 생성
	 * @param cond 검색 조건
	 * @return BooleanBuilder
	 */
	private static BooleanBuilder getBooleanBuilder(ReviewSearchCond cond) {
		BooleanBuilder builder = new BooleanBuilder();
		
		if (cond.getStoreName() != null) {
			builder.and(review.store.name.like("%" + cond.getStoreName() + "%"));
		}
		if (cond.getAddress() != null) {
			builder.and(review.store.address.like("%" + cond.getAddress() + "%"));
		}
		if (cond.getRestaurantType() != null) {
			builder.and(review.store.type.stringValue().like(cond.getRestaurantType()));
		}
		
		return builder;
	}
}
