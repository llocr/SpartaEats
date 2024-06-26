package like.heocholi.spartaeats.domain.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import like.heocholi.spartaeats.domain.menu.dto.MenuResponseDto;
import like.heocholi.spartaeats.domain.menu.entity.Menu;
import like.heocholi.spartaeats.domain.menu.exception.MenuException;
import like.heocholi.spartaeats.domain.menu.repository.MenuRepository;
import like.heocholi.spartaeats.domain.store.service.StoreService;
import like.heocholi.spartaeats.global.exception.ErrorType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreService storeService;

    /**
     * 메뉴 단일 조회
     * @param storeId
     * @param menuId
     * @return 메뉴
     */
    public MenuResponseDto getMenu(Long storeId, Long menuId) {
        storeService.findStoreById(storeId);
        Menu menu = findMenu(storeId, menuId);
        
        return new MenuResponseDto(menu);
    }
    
    /**
     * 메뉴 전체 조회
     * @param storeId
     * @return 메뉴 목록
     */
    public List<MenuResponseDto> getMenus(Long storeId) {
        storeService.findStoreById(storeId);
        List<MenuResponseDto> menuList = menuRepository.findAllByStoreId(storeId).stream().map(MenuResponseDto::new).toList();

        if(menuList.isEmpty()){
            throw new MenuException(ErrorType.NOT_FOUND_MENUS);
        }
        
        return menuList;
    }
    
    /* util */
    
    /**
     * 메뉴 조회
     * @param storeId
     * @param menuId
     * @return 메뉴
     */
    private Menu findMenu(Long storeId, Long menuId) {
        return menuRepository.findByStoreIdAndId(storeId, menuId).orElseThrow(() -> new MenuException(ErrorType.NOT_FOUND_MENU));
    }


}
