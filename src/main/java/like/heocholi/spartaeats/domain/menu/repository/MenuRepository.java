package like.heocholi.spartaeats.domain.menu.repository;

import like.heocholi.spartaeats.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByStoreId(Long storeId);
    Optional<Menu> findByStoreIdAndId(Long storeId, Long menuId);
}
