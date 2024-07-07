package like.heocholi.spartaeats.domain.customer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import like.heocholi.spartaeats.domain.customer.dto.CustomerResponseDTO;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.customer.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CustomerServiceTest {
	@Mock
	CustomerRepository customerRepository;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@InjectMocks
	CustomerService customerService;
	
	private Customer customer;
	
	@BeforeEach
	void setUp() {
		customer = Customer.builder()
			.userId("test1")
			.password(passwordEncoder.encode("testtest12!"))
			.name("test1")
			.address("서울시 강남구 역삼동")
			.build();
		
		customerRepository.save(customer);
	}
	
	@Test
	@DisplayName("프로필 조회 테스트")
	void 프로필조회() {
	    //when
		lenient().when(customerRepository.findLikeReviewCount(anyLong())).thenReturn(anyLong());
		CustomerResponseDTO responseDTO = customerService.getCustomerInfo(customer);
		
		//then
		assertThat(responseDTO).isNotNull();
		assertThat(responseDTO.getCustomerId()).isEqualTo(customer.getUserId());
		assertThat(responseDTO.getName()).isEqualTo(customer.getName());
		assertThat(responseDTO.getAddress()).isEqualTo(customer.getAddress());
		assertThat(responseDTO.getLikeReviewCount()).isEqualTo(0);
	}

}