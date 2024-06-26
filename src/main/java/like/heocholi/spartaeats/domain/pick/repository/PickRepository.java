package like.heocholi.spartaeats.domain.pick.repository;

import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.pick.entity.Pick;
import like.heocholi.spartaeats.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickRepository extends JpaRepository<Pick, Long> {
    Pick findByStoreAndCustomer(Store store, Customer customer);

    Page<Pick> findAllByCustomerAndIsPickTrue(Customer customer, Pageable pageable);
}
