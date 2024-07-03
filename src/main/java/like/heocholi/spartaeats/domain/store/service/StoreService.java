package like.heocholi.spartaeats.domain.store.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import like.heocholi.spartaeats.domain.common.util.page.PageUtil;
import like.heocholi.spartaeats.domain.menu.exception.MenuException;
import like.heocholi.spartaeats.domain.pick.repository.PickRepository;
import like.heocholi.spartaeats.domain.store.dto.StorePageResponseDto;
import like.heocholi.spartaeats.domain.store.dto.StoreResponseDto;
import like.heocholi.spartaeats.domain.store.dto.StoreTopPickResponseDTO;
import like.heocholi.spartaeats.domain.store.entity.Store;
import like.heocholi.spartaeats.domain.store.enums.RestaurantType;
import like.heocholi.spartaeats.domain.store.exception.StoreException;
import like.heocholi.spartaeats.domain.store.repository.StoreRepository;
import like.heocholi.spartaeats.global.exception.ErrorType;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;
    private final PickRepository pickRepository;

    /**
     * 가게 단건 조회
     * @param storeId
     * @return 가게 정보
     */
    public StoreResponseDto readStore(Long storeId) {
        Store store = findStoreById(storeId);
        return new StoreResponseDto(store);
    }

    /**
     * 가게 리스트 조회
     * @param page
     * @return 가게 리스트
     */
    public StorePageResponseDto getStorePageByType(String type, Integer page) {
        RestaurantType restaurantType = checkValidateType(type);
        Pageable pageable = PageUtil.createPageable(page, Sort.by("createdAt").descending());

        Page<Store> storePageList = storeRepository.findByTypeGroupedByStoreOrderByOrderCountDesc(restaurantType, pageable);

        PageUtil.checkValidatePage(page, storePageList);

        return new StorePageResponseDto(page, storePageList);
    }
    
    /**
     * 인기 가게 리스트 조회
     * @return 인기 가게 리스트
     */
    public List<StoreTopPickResponseDTO> getTopPicks() {
        List<StoreTopPickResponseDTO> topPicks = storeRepository.findTopPicks();
        
        if (topPicks.isEmpty()) {
            throw new StoreException(ErrorType.NOT_FOUND_STORE);
        }
        
        return topPicks;
    }
    
    /* util */
    
    /**
     * 가게 조회
     * @param storeId
     * @return 가게 정보
     */
    public Store findStoreById(Long storeId) {
        Store store =storeRepository.findById(storeId).orElseThrow(() -> new MenuException(ErrorType.NOT_FOUND_STORE));
        
        return store;
    }
    
    /**
     * 가게 타입 유효성 검사
     * @param type
     * @return 가게 타입
     */
    private RestaurantType checkValidateType(String type) {
        RestaurantType restaurantType;
        try {
            restaurantType = RestaurantType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new StoreException(ErrorType.INVALID_TYPE);
        }
        
        return restaurantType;
    }
}
