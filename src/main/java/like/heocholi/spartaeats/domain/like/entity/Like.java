package like.heocholi.spartaeats.domain.like.entity;

import jakarta.persistence.*;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "likes")
public class Like {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // 좋아요 ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id", nullable = false)
	private Review review;  // 리뷰와의 다대일 관계

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;  // 고객과의 다대일 관계

	private boolean isLike;  // 좋아요 상태 (true: 좋아요, false: 좋아요 취소)

	@Builder
	public Like (Customer customer, Review review, boolean isLike) {
		this.customer = customer;
		this.review = review;
		this.isLike = isLike;
	}

	public void update() {
		this.isLike = !this.isLike;
	}
}
