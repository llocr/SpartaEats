package like.heocholi.spartaeats.domain.pick.controller;

import like.heocholi.spartaeats.domain.pick.dto.PickPageResponseDto;
import like.heocholi.spartaeats.domain.common.dto.ResponseMessage;
import like.heocholi.spartaeats.global.security.UserDetailsImpl;
import like.heocholi.spartaeats.domain.pick.service.PickService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/picks")
public class PickController {

    private final PickService pickService;

    @GetMapping
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