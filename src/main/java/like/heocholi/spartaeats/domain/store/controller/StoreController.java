package like.heocholi.spartaeats.domain.store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import like.heocholi.spartaeats.domain.common.dto.ResponseMessage;
import like.heocholi.spartaeats.domain.store.dto.StorePageResponseDto;
import like.heocholi.spartaeats.domain.store.dto.StoreResponseDto;
import like.heocholi.spartaeats.domain.store.dto.StoreTopPickResponseDTO;
import like.heocholi.spartaeats.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {
    private final StoreService storeService;

    /**
     * 가게 상세 정보 보기
     * @param storeId
     * @return ResponseEntity<ResponseMessage<StoreResponseDto>>
     *     - statusCode: 200
     *     - message: "가게 상세 정보를 성공적으로 불러왔습니다."
     *     - data: 가게 상세 정보
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<ResponseMessage<StoreResponseDto>> readStore(@PathVariable Long storeId) {
        StoreResponseDto responseDto = storeService.readStore(storeId);

        ResponseMessage<StoreResponseDto> responseMessage = ResponseMessage.<StoreResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message(storeId + "번 가게 상세 정보를 성공적으로 불러왔습니다.")
                .data(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);

    }

    /**
     * 가게 리스트 불러오기
     * @param type
     * @param page
     * @return ResponseEntity<ResponseMessage<StorePageResponseDto>>
     *     - statusCode: 200
     *     - message: "page번 페이지 가게 리스트를 성공적으로 불러왔습니다."
     *     - data: 가게 리스트
     */
    @GetMapping
    public ResponseEntity<ResponseMessage<StorePageResponseDto>> getStorePage (
            @RequestParam String type,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        StorePageResponseDto storePageResponseDto = storeService.getStorePageByType(type, page);

        ResponseMessage<StorePageResponseDto> responseMessage = ResponseMessage.<StorePageResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message(page + "번 페이지 가게 리스트를 성공적으로 불러왔습니다.")
                .data(storePageResponseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
    
    /**
     * 찜하기 TOP 10 가게 리스트 불러오기
     * @return ResponseEntity<ResponseMessage<List<StoreTopPickResponseDTO>>>
     *     - statusCode: 200
     *     - message: "찜하기 TOP 10 가게 리스트를 성공적으로 불러왔습니다."
     *     - data: 찜하기 TOP 10 가게 리스트
     */
    @GetMapping("/top-picks")
    public ResponseEntity<ResponseMessage<List<StoreTopPickResponseDTO>>> getTopPicks() {
        List<StoreTopPickResponseDTO> responseDtoList = storeService.getTopPicks();

        ResponseMessage<List<StoreTopPickResponseDTO>> responseMessage = ResponseMessage.<List<StoreTopPickResponseDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("찜하기 TOP 10 가게 리스트를 성공적으로 불러왔습니다.")
                .data(responseDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
