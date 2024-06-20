package like.heocholi.spartaeats.controller;

import like.heocholi.spartaeats.dto.ResponseMessage;
import like.heocholi.spartaeats.entity.Menu;
import like.heocholi.spartaeats.service.MenuService;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MenuController {

    MenuService menuService;

    // R
    //메뉴 단건 조회
    @GetMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<ResponseMessage> getMenu(@PathVariable Long storeId, @PathVariable Long menuId) {
        Menu menu = menuService.getMenu(storeId,menuId);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseMessage.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(menu.getStore().getName() +"에"+menu.getName() + "가 조회가 완료되었습니다.")
                        .data(menu)
                        .build()
        );
    }

    //메뉴 전체 조회
    @GetMapping("/stores/{storeId}/menus")
    public ResponseEntity<ResponseMessage> getMenus(@PathVariable Long storeId) {
        List<Menu> menus = menuService.getMenus(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseMessage.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(menus.get(0).getStore().getName() +"에 모든 메뉴 조회가 완료되었습니다.")
                        .data(menus)
                        .build()
        );
    }


}