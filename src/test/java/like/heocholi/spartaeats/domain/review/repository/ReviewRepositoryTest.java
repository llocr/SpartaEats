package like.heocholi.spartaeats.domain.review.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import like.heocholi.spartaeats.domain.common.util.page.PageUtil;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.customer.repository.CustomerRepository;
import like.heocholi.spartaeats.domain.like.entity.Like;
import like.heocholi.spartaeats.domain.like.repository.LikeRepository;
import like.heocholi.spartaeats.domain.manager.entity.Manager;
import like.heocholi.spartaeats.domain.manager.repository.ManagerRepository;
import like.heocholi.spartaeats.domain.order.entity.Order;
import like.heocholi.spartaeats.domain.order.repository.OrderRepository;
import like.heocholi.spartaeats.domain.pick.entity.Pick;
import like.heocholi.spartaeats.domain.pick.repository.PickRepository;
import like.heocholi.spartaeats.domain.review.entity.Review;
import like.heocholi.spartaeats.domain.store.entity.Store;
import like.heocholi.spartaeats.domain.store.repository.StoreRepository;
import like.heocholi.spartaeats.global.config.QueryDslConfig;

@DataJpaTest
@Transactional
@Import(QueryDslConfig.class)
@ActiveProfiles("test")
class ReviewRepositoryTest {
	@Autowired
	ReviewRepository reviewRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	StoreRepository storeRepository;
	
	@Autowired
	LikeRepository likeRepository;
	
	@Autowired
	PickRepository pickRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ManagerRepository managerRepository;
	
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
		customerRepository.save(customer1);
		
		customer2 = Customer.builder()
			.userId("test2")
			.password("testtest12!")
			.name("test2")
			.address("서울시 강남구 역삼동")
			.build();
		customerRepository.save(customer2);
		
		manager = Manager.builder()
			.userId("manager1")
			.password("testtest12!")
			.build();
		managerRepository.save(manager);
		
		store = Store.builder()
			.name("store1")
			.manager(manager)
			.address("서울시 강남구 역삼동")
			.build();
		storeRepository.save(store);
		
		order = Order.builder()
			.customer(customer1)
			.store(store)
			.build();
		orderRepository.save(order);
		
		review = Review.builder()
			.order(order)
			.store(store)
			.customer(customer1)
			.contents("맛있어요")
			.build();
		reviewRepository.save(review);
		
		Like like = Like.builder()
			.customer(customer2)
			.review(review)
			.isLike(true)
			.build();
		likeRepository.save(like);
		
		Pick pick = Pick.builder()
			.customer(customer2)
			.store(store)
			.build();
		pickRepository.save(pick);
	}
	
	@Test
	@DisplayName("좋아요 누른 리뷰 조회")
	void 좋아요누른리뷰조회() {
	    //given
		Long customerId = customer2.getId();
		Pageable pageable = PageUtil.createPageable(1, Sort.by("createdAt").descending());
		
	    //when
		Page<Review> likeReview = reviewRepository.findLikeReview(customerId, pageable);
		
		//then
		assertThat(likeReview.getContent().size()).isEqualTo(1);
		assertThat(likeReview.getContent().get(0).getCustomer().getId()).isEqualTo(customer1.getId());
	}
	
}