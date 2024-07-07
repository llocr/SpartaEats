package like.heocholi.spartaeats.domain.review.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.like.entity.Like;
import like.heocholi.spartaeats.domain.manager.entity.Manager;
import like.heocholi.spartaeats.domain.order.entity.Order;
import like.heocholi.spartaeats.domain.pick.entity.Pick;
import like.heocholi.spartaeats.domain.review.dto.ReviewListResponseDTO;
import like.heocholi.spartaeats.domain.review.dto.ReviewSearchCond;
import like.heocholi.spartaeats.domain.review.entity.Review;
import like.heocholi.spartaeats.domain.review.repository.ReviewRepository;
import like.heocholi.spartaeats.domain.store.entity.Store;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ReviewServiceTest {
	@Mock
	ReviewRepository reviewRepository;
	
	@InjectMocks
	ReviewService reviewService;
	
	private Customer customer1;
	private Customer customer2;
	private Manager manager;
	private Store store;
	private Review review;
	private Order order;
	
	@BeforeEach
	void setUp() {
		customer1 = Customer.builder()
			.userId("test1")
			.password("testtest12!")
			.name("test1")
			.address("서울시 강남구 역삼동")
			.build();
		
		customer2 = Customer.builder()
			.userId("test2")
			.password("testtest12!")
			.name("test2")
			.address("서울시 강남구 역삼동")
			.build();
		
		manager = Manager.builder()
			.userId("manager1")
			.password("testtest12!")
			.build();
		
		store = Store.builder()
			.name("store1")
			.manager(manager)
			.address("서울시 강남구 역삼동")
			.build();
		
		order = Order.builder()
			.customer(customer1)
			.store(store)
			.build();
		
		review = Review.builder()
			.order(order)
			.store(store)
			.customer(customer1)
			.contents("맛있어요")
			.build();
	}
	
	@Test
	@DisplayName("좋아요 누른 리뷰 조회")
	void 좋아요누른리뷰조회() {
		//given
		Integer page = 1;
		Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
		Page<Review> reviewPage = new PageImpl<>(List.of(review), pageable, 1);
		
		when(reviewRepository.findLikeReview(any(), any())).thenReturn(reviewPage);
		
		//when
		ReviewListResponseDTO reviewListResponseDTO = reviewService.likeReviews(page, customer2);
		
		//then
		assertThat(reviewListResponseDTO).isNotNull();
		assertThat(reviewListResponseDTO.getReviewList()).isNotEmpty();
		assertThat(reviewListResponseDTO.getCurrentPage()).isEqualTo(page);
	}
	
	@Test
	@DisplayName("찜한 가게의 리뷰 조회")
	void 찜한가게리뷰조회() {
	    //given
	    Integer page = 1;
		String sort = "userId";
		ReviewSearchCond cond = new ReviewSearchCond();
		cond.setAddress("서울");
		
		Pageable pageable = PageRequest.of(page, 5, Sort.by(sort));
		Page<Review> reviewPage = new PageImpl<>(List.of(review), pageable, 1);
		
		when(reviewRepository.findPickReview(any(), any(), any())).thenReturn(reviewPage);
		
	    //when
		ReviewListResponseDTO reviewListResponseDTO = reviewService.pickReviews(page, sort, cond, customer2);
	    
	    //then
		assertThat(reviewListResponseDTO).isNotNull();
		assertThat(reviewListResponseDTO.getReviewList()).isNotEmpty();
		assertThat(reviewListResponseDTO.getCurrentPage()).isEqualTo(page);
	}
}