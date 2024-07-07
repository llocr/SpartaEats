package like.heocholi.spartaeats.domain.pick.service;

import like.heocholi.spartaeats.domain.common.util.page.PageUtil;
import like.heocholi.spartaeats.domain.store.dto.PickStoreResponseDto;
import like.heocholi.spartaeats.domain.store.entity.Store;
import like.heocholi.spartaeats.domain.store.service.StoreService;
import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.domain.pick.dto.PickPageResponseDto;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.pick.entity.Pick;
import like.heocholi.spartaeats.global.exception.PageException;
import like.heocholi.spartaeats.domain.pick.exception.PickException;
import like.heocholi.spartaeats.domain.pick.repository.PickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PickService {
    private final PickRepository pickRepository;
    private final StoreService storeService;
    
    /**
     * 가게 찜하기
     * @param storeId
     * @param customer
     * @return 찜하기 결과
     */
    @Transactional
    public PickStoreResponseDto managePicks(Long storeId, Customer customer) {
        Store store = storeService.findStoreById(storeId);
        Pick pick = updatePick(store, customer);
        return new PickStoreResponseDto(store.getName(), pick.isPick());
        
    }
    
    /**
     * 찜하기 업데이트
     * @param store
     * @param customer
     * @return 찜하기 정보
     */
    public Pick updatePick(Store store, Customer customer) {
        Pick pick = pickRepository.findByStoreAndCustomer(store, customer);
        
        if (pick != null) {
            pick.update();
        } else{
            pick = Pick.builder()
                .customer(customer)
                .store(store)
                .isPick(true)
                .build();
            
            pickRepository.save(pick);
        }
        
        return pick;
    }
    

    /**
     * 찜하기 리스트 불러오기
     * @param customer
     * @param page
     * @return 찜하기 리스트
     */
    public PickPageResponseDto getPickList(Customer customer, Integer page) {
        Pageable pageable = PageUtil.createPageable(page, Sort.by("createdAt").descending());
        Page<Pick> pickPage = pickRepository.findAllByCustomerAndIsPickTrue(customer, pageable);
        PageUtil.checkValidatePage(page, pickPage);

        return new PickPageResponseDto(page, pickPage);
    }
    
}
