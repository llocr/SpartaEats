package like.heocholi.spartaeats.domain.customer.dto;

import like.heocholi.spartaeats.domain.customer.entity.Customer;
import lombok.Getter;

@Getter
public class SignupResponseDto {
    private String userId;
    private String name;
    private String address;

    public SignupResponseDto(Customer customer) {
        this.userId = customer.getUserId();
        this.name = customer.getName();
        this.address = customer.getAddress();
    }
}
