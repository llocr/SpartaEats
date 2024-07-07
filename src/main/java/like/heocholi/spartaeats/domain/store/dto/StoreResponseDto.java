package like.heocholi.spartaeats.domain.store.dto;

import like.heocholi.spartaeats.domain.menu.entity.Menu;
import like.heocholi.spartaeats.domain.store.entity.Store;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class StoreResponseDto {

    private String name;
    private String address;
    private List<MenuDto> menuList;

    public StoreResponseDto(Store store) {
        this.name = store.getName();
        this.address = store.getAddress();
        this.menuList = store.getMenuList().stream()
                .map(MenuDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class MenuDto {

        private String name;
        private int price;

        public MenuDto(Menu menu) {
            this.name = menu.getName();
            this.price = menu.getPrice();
        }
    }

}
