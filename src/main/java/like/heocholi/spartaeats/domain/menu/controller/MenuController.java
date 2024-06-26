package like.heocholi.spartaeats.domain.menu.controller;

import like.heocholi.spartaeats.domain.menu.dto.MenuResponseDto;
import like.heocholi.spartaeats.domain.common.dto.ResponseMessage;
import like.heocholi.spartaeats.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/menus")
public class MenuController {
    private final MenuService menuService;

    /**
     * 메뉴 단일 조회
     * @param storeId
     * @param menuId
     * @return ResponseEntity<ResponseMessage<MenuResponseDto>>
     *     - statusCode: 200
     *     - message: "[가게 이름]에 [메뉴 이름] 메뉴 조회가 완료되었습니다."
     *     - data: 메뉴 정보
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<ResponseMessage<MenuResponseDto>> getMenu(@PathVariable Long storeId, @PathVariable Long menuId) {
        MenuResponseDto responseDto = menuService.getMenu(storeId, menuId);

        ResponseMessage<MenuResponseDto> responseMessage = ResponseMessage.<MenuResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("["+responseDto.getStoreName() + "]에 ["+ responseDto.getName()+"] 메뉴 조회가 완료되었습니다.")
                        .data(responseDto)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 메뉴 전체 조회
     * @param storeId
     * @return ResponseEntity<ResponseMessage<List<MenuResponseDto>>>
     *     - statusCode: 200
     *     - message: "[가게 이름]의 모든 메뉴 조회가 완료되었습니다."
     *     - data: 메뉴 목록
     */
    @GetMapping
    public ResponseEntity<ResponseMessage<List<MenuResponseDto>>> getMenus(@PathVariable Long storeId) {
        List<MenuResponseDto> menuResponseDtoList = menuService.getMenus(storeId);

        ResponseMessage<List<MenuResponseDto>> responseMessage = ResponseMessage.<List<MenuResponseDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("["+menuResponseDtoList.get(0).getStoreName() +"]의 모든 메뉴 조회가 완료되었습니다.")
                .data(menuResponseDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }


}
