package like.heocholi.spartaeats.domain.store.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import like.heocholi.spartaeats.domain.store.dto.StoreTopPickResponseDTO;

@Repository
public interface StoreRepositoryCustom {
	List<StoreTopPickResponseDTO> findTopPicks();
}
