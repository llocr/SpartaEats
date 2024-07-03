package like.heocholi.spartaeats.domain.customer.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.global.config.QueryDslConfig;

@DataJpaTest
@Transactional
@Import(QueryDslConfig.class)
@ActiveProfiles("test")
class CustomerRepositoryTest {
	@Autowired
	CustomerRepository customerRepository;
	
	private Customer customer;
	
	@BeforeEach
	void setUp() {
		customer = Customer.builder()
			.userId("test1")
			.password("testtest12!")
			.name("test1")
			.address("서울시 강남구 역삼동")
			.build();
		
		customerRepository.save(customer);
	}
	
	@Test
	@DisplayName("리뷰 좋아요 개수 가져오기")
	void 리뷰좋아요개수가져오기() {
	    //given
	    Long userId = customer.getId();
		
	    //when
		long likeReviewCount = customerRepository.findLikeReviewCount(userId);
		
		//then
		Assertions.assertThat(likeReviewCount).isEqualTo(0);
		
	}
}