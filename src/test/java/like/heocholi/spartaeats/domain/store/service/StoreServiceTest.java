package like.heocholi.spartaeats.domain.store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import like.heocholi.spartaeats.domain.manager.entity.Manager;
import like.heocholi.spartaeats.domain.store.dto.StoreTopPickResponseDTO;
import like.heocholi.spartaeats.domain.store.entity.Store;
import like.heocholi.spartaeats.domain.store.exception.StoreException;
import like.heocholi.spartaeats.domain.store.repository.StoreRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class StoreServiceTest {
	@Mock
	StoreRepository storeRepository;
	
	@InjectMocks
	StoreService storeService;
	
	private Manager manager;
	private Store store1;
	private Store store2;
	private Store store3;
	private Store store4;

	
	@BeforeEach
	void setUp() {
		manager = Manager.builder()
			.userId("manager1")
			.password("testtest12!")
			.build();
		
		store1 = Store.builder()
			.name("store1")
			.manager(manager)
			.address("서울시 강남구 역삼동")
			.build();
		
		store2 = Store.builder()
			.name("store2")
			.manager(manager)
			.address("서울시 강남구 역삼동")
			.build();
		
		store3 = Store.builder()
			.name("store3")
			.manager(manager)
			.address("서울시 강남구 역삼동")
			.build();
		
		store4 = Store.builder()
			.name("store1")
			.manager(manager)
			.address("서울시 강남구 역삼동")
			.build();
	}
	
	@Test
	@DisplayName("인기 가게 리스트 조회")
	void 인기가게리스트조회() {
	    //given
		List<Store> stores = List.of(store1, store2, store3, store4);
		List<StoreTopPickResponseDTO> storeTopPickResponseDTOS = stores.stream().map(s -> new StoreTopPickResponseDTO(
			s.getName(),
			s.getAddress(),
			5
		)).toList();
		
		when(storeRepository.findTopPicks()).thenReturn(storeTopPickResponseDTOS);
		
	    //when
		List<StoreTopPickResponseDTO> topPicks = storeService.getTopPicks();
	    
	    //then
		assertThat(topPicks).isNotNull();
		assertThat(topPicks.size()).isEqualTo(4);
		assertThat(topPicks.get(0).getName()).isEqualTo(store1.getName());
	}
	
	@Test
	@DisplayName("인기 가게 리스트 조회 실패 - 가게 없음")
	void 인기가게리스트조회실패_가게없음() {
		// given
		List<Store> stores = Arrays.asList();
		List<StoreTopPickResponseDTO> storeTopPickResponseDTOS = stores.stream().map(s -> new StoreTopPickResponseDTO(
			s.getName(),
			s.getAddress(),
			5
		)).toList();
		
		when(storeRepository.findTopPicks()).thenReturn(storeTopPickResponseDTOS);
		
		// when & then
		assertThatThrownBy(() -> storeService.getTopPicks())
			.isInstanceOf(StoreException.class);
	}
}