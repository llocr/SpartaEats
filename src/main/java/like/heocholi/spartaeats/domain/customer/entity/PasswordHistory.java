package like.heocholi.spartaeats.domain.customer.entity;

import jakarta.persistence.*;
import like.heocholi.spartaeats.domain.common.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "password_history")
public class PasswordHistory extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String password;

    @Builder
    public PasswordHistory (Customer customer, String password) {
        this.customer = customer;
        this.password = password;
    }
}
