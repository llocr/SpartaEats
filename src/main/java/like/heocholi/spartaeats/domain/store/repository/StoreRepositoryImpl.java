package like.heocholi.spartaeats.domain.store.repository;

import static like.heocholi.spartaeats.domain.pick.entity.QPick.pick;
import static like.heocholi.spartaeats.domain.store.entity.QStore.store;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import like.heocholi.spartaeats.domain.store.dto.StoreTopPickResponseDTO;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	
	@Override
	public List<StoreTopPickResponseDTO> findTopPicks() {
		List<Tuple> topPicks = queryFactory
			.select(store.name, store.address, pick.count())
			.from(pick)
			.join(store).on(pick.store.id.eq(store.id))
			.where(pick.store.isNotNull())
			.groupBy(store.id)
			.orderBy(pick.count().desc())
			.limit(10)
			.fetch();
		
		return topPicks.stream().map(tuple -> new StoreTopPickResponseDTO(
			tuple.get(store.name),
			tuple.get(store.address),
			tuple.get(pick.count())
		)).toList();
	}
}
