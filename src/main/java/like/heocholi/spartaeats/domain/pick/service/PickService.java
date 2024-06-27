package like.heocholi.spartaeats.domain.pick.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PickService {
    private final PickRepository pickRepository;

    /**
     * 찜하기 리스트 불러오기
     * @param customer
     * @param page
     * @return 찜하기 리스트
     */
    public PickPageResponseDto getPickList(Customer customer, Integer page) {
        Pageable pageable = PageRequest.of(page-1, 5);
        Page<Pick> pickPage = pickRepository.findAllByCustomerAndIsPickTrue(customer, pageable);
        checkValidatePage(page, pickPage);

        return new PickPageResponseDto(page, pickPage);
    }

    /**
     * 페이지 유효성 검사
     * @param page
     * @param pickPage
     */
    private static void checkValidatePage(Integer page, Page<Pick> pickPage) {
        if (pickPage.getTotalElements() == 0) {
            throw new PickException(ErrorType.NOT_FOUND_PICK);
        }

        if (page > pickPage.getTotalPages() || page < 1) {
            throw new PageException(ErrorType.INVALID_PAGE);
        }
    }
}
