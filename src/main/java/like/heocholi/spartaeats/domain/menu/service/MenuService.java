package like.heocholi.spartaeats.domain.menu.service;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.domain.menu.dto.MenuResponseDto;
import like.heocholi.spartaeats.domain.menu.entity.Menu;
import like.heocholi.spartaeats.domain.store.entity.Store;
import like.heocholi.spartaeats.domain.menu.exception.MenuException;
import like.heocholi.spartaeats.domain.menu.repository.MenuRepository;
import like.heocholi.spartaeats.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    // 메뉴 상세 조회
    public MenuResponseDto getMenu(Long storeId, Long menuId) {
        Store store = findStoreById(storeId);
        Menu menu = menuRepository.findByStoreIdAndId(storeId,menuId).orElseThrow(() -> new MenuException(ErrorType.NOT_FOUND_MENU));

        return new MenuResponseDto(menu);
    }

    // 메뉴 전체 조회
    public List<MenuResponseDto> getMenus(Long storeId) {
        Store store = findStoreById(storeId);
        List<MenuResponseDto> menuList = menuRepository.findAllByStoreId(storeId).stream().map(MenuResponseDto::new).toList();

        if(menuList.isEmpty()){
            throw new MenuException(ErrorType.NOT_FOUND_MENUS);
        }

       return menuList;
    }
    
    // 가게 조회
    public Store findStoreById(Long storeId) {
        Store store =storeRepository.findById(storeId).orElseThrow(() -> new MenuException(ErrorType.NOT_FOUND_STORE));

        return store;
    }


}
