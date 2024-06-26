package like.heocholi.spartaeats.domain.store.repository;

import like.heocholi.spartaeats.domain.store.enums.RestaurantType;
import like.heocholi.spartaeats.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("SELECT s FROM Store s LEFT JOIN s.orders o WHERE s.type = :type GROUP BY s.id ORDER BY COUNT(o) DESC")
    Page<Store> findByTypeGroupedByStoreOrderByOrderCountDesc(@Param("type") RestaurantType type, Pageable pageable);
}
