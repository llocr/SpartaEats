package like.heocholi.spartaeats.domain.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import like.heocholi.spartaeats.domain.manager.entity.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
