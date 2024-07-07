package like.heocholi.spartaeats.domain.store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.customer.repository.CustomerRepository;
import like.heocholi.spartaeats.domain.manager.entity.Manager;
import like.heocholi.spartaeats.domain.manager.repository.ManagerRepository;
import like.heocholi.spartaeats.domain.pick.entity.Pick;
import like.heocholi.spartaeats.domain.pick.repository.PickRepository;
import like.heocholi.spartaeats.domain.store.dto.StoreTopPickResponseDTO;
import like.heocholi.spartaeats.domain.store.entity.Store;
import like.heocholi.spartaeats.global.config.QueryDslConfig;

@DataJpaTest
@Transactional
@Import(QueryDslConfig.class)
@ActiveProfiles("test")
class StoreRepositoryTest {
	@Autowired
	StoreRepository storeRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	ManagerRepository managerRepository;
	
	@Autowired
	PickRepository pickRepository;
	
	private Customer customer1;
	private Customer customer2;
	private Customer customer3;
	private Customer customer4;
	private Manager manager;
	private Store store1;
	private Store store2;
	private Store store3;
	private Store store4;
	
	
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
		
		customer3 = Customer.builder()
			.userId("test3")
			.password("testtest12!")
			.name("test3")
			.address("서울시 강남구 역삼동")
			.build();
		customerRepository.save(customer3);
		
		customer4 = Customer.builder()
			.userId("test4")
			.password("testtest12!")
			.name("test4")
			.address("서울시 강남구 역삼동")
			.build();
		customerRepository.save(customer4);
		
		manager = Manager.builder()
			.userId("manager1")
			.password("testtest12!")
			.build();
		managerRepository.save(manager);
		
		store1 = Store.builder()
			.name("store1")
			.manager(manager)
			.address("서울시 강남구 역삼동")
			.build();
		storeRepository.save(store1);
		
		store2 = Store.builder()
			.name("store2")
			.manager(manager)
			.address("서울시 강남구 역삼동")
			.build();
		storeRepository.save(store2);
		
		store3 = Store.builder()
			.name("store3")
			.manager(manager)
			.address("서울시 강남구 역삼동")
			.build();
		storeRepository.save(store3);
		
		store4 = Store.builder()
			.name("store4")
			.manager(manager)
			.address("서울시 강남구 역삼동")
			.build();
		storeRepository.save(store4);
		
		Pick pick1 = Pick.builder()
			.customer(customer1)
			.store(store1)
			.isPick(true)
			.build();
		pickRepository.save(pick1);
		
		Pick pick2 = Pick.builder()
			.customer(customer2)
			.store(store2)
			.isPick(true)
			.build();
		pickRepository.save(pick2);
		
		Pick pick3 = Pick.builder()
			.customer(customer3)
			.store(store3)
			.isPick(true)
			.build();
		pickRepository.save(pick3);
		
		Pick pick4 = Pick.builder()
			.customer(customer4)
			.store(store4)
			.isPick(true)
			.build();
		pickRepository.save(pick4);
		
		Pick pick5 = Pick.builder()
			.customer(customer1)
			.store(store2)
			.isPick(true)
			.build();
		pickRepository.save(pick5);
		
		Pick pick6 = Pick.builder()
			.customer(customer2)
			.store(store3)
			.isPick(true)
			.build();
		pickRepository.save(pick6);
		
		Pick pick7 = Pick.builder()
			.customer(customer3)
			.store(store3)
			.isPick(true)
			.build();
		pickRepository.save(pick7);
	}
	
	@Test
	@DisplayName("인기 가게 리스트 조회")
	void 인기가게리스트조회() {
		//when
		List<StoreTopPickResponseDTO> topPicks = storeRepository.findTopPicks();
		
		//then
		assertThat(topPicks).isNotNull();
		assertThat(topPicks.size()).isEqualTo(4);
		assertThat(topPicks.get(0).getName()).isEqualTo("store3");
		assertThat(topPicks.get(1).getName()).isEqualTo("store2");
		assertThat(topPicks.get(2).getName()).isEqualTo("store1");
		assertThat(topPicks.get(2).getName()).isEqualTo("store4");
	}
}