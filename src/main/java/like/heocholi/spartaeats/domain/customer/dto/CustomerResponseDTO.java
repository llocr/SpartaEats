package like.heocholi.spartaeats.domain.customer.dto;

import like.heocholi.spartaeats.domain.customer.entity.Customer;
import lombok.Getter;

@Getter
public class CustomerResponseDTO {
    private String customerId;
    private String name;
    private String address;
    private String bio;
    private long likeReviewCount;

    public CustomerResponseDTO(Customer customer, long likeReviewCount) {
        this.customerId = customer.getUserId();
        this.name = customer.getName();
        this.address = customer.getAddress();
        this.bio = customer.getBio();
        this.likeReviewCount = likeReviewCount;
    }
}
