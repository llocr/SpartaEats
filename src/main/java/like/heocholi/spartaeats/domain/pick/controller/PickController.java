package like.heocholi.spartaeats.domain.pick.controller;

import like.heocholi.spartaeats.domain.pick.dto.PickPageResponseDto;
import like.heocholi.spartaeats.domain.common.dto.ResponseMessage;
import like.heocholi.spartaeats.domain.store.dto.PickStoreResponseDto;
import like.heocholi.spartaeats.global.security.UserDetailsImpl;
import like.heocholi.spartaeats.domain.pick.service.PickService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PickController {
    private final PickService pickService;
    
    /**
     * 가게 찜하기
     * @param storeId
     * @param userDetails
     * @return ResponseEntity<ResponseMessage<PickStoreResponseDto>>
     *     - statusCode: 200
     *     - message: "storeId번 가게 찜 등록 / 취소가 완료되었습니다."
     *     - data: 찜하기 정보
     */
    @PostMapping("/stores/{storeId}/pick")
    public ResponseEntity<ResponseMessage<PickStoreResponseDto>> managePicks
    (@PathVariable Long storeId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PickStoreResponseDto responseDto = pickService.managePicks(storeId, userDetails.getCustomer());
        
        ResponseMessage<PickStoreResponseDto> responseMessage = ResponseMessage.<PickStoreResponseDto>builder()
            .statusCode(HttpStatus.OK.value())
            .message(storeId + "번 가게 찜 등록 / 취소가 완료되었습니다. ")
            .data(responseDto)
            .build();
        
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
    

    /**
     * 찜하기 리스트 불러오기
     * @param userDetails
     * @param page
     * @return ResponseEntity<ResponseMessage<PickPageResponseDto>>
     *     - statusCode: 200
     *     - message: "찜하기 리스트를 성공적으로 불러왔습니다."
     *     - data: 찜하기 리스트
     */
    @GetMapping("/picks")
    public ResponseEntity<ResponseMessage<PickPageResponseDto>> getPickList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        PickPageResponseDto pickPageResponseDto = pickService.getPickList(userDetails.getCustomer(), page);

        ResponseMessage<PickPageResponseDto> responseMessage = ResponseMessage.<PickPageResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message("찜하기 리스트를 성공적으로 불러왔습니다.")
                .data(pickPageResponseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

}
