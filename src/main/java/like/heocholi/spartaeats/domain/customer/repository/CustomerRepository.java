package like.heocholi.spartaeats.domain.customer.repository;

import like.heocholi.spartaeats.domain.customer.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
    Optional<Customer> findByUserId(String username);
}