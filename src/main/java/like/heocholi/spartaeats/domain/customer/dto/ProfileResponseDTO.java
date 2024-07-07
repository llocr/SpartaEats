package like.heocholi.spartaeats.domain.customer.dto;

import like.heocholi.spartaeats.domain.customer.entity.Customer;
import lombok.Getter;

@Getter
public class ProfileResponseDTO {
    String customerId;
    String name;
    String bio;

    public ProfileResponseDTO(Customer customer) {
        this.customerId = customer.getUserId();
        this.name = customer.getName();
        this.bio = customer.getBio();
    }
}
