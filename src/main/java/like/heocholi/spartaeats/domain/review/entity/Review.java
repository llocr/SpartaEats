package like.heocholi.spartaeats.domain.review.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.order.entity.Order;
import like.heocholi.spartaeats.domain.store.entity.Store;
import like.heocholi.spartaeats.domain.common.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reviews")
@NoArgsConstructor
public class Review extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	private String contents;

	private int likeCount;

	public void updateLike(boolean result) {
		this.likeCount = result ? this.likeCount + 1 : this.likeCount - 1;
	}

	// 새로운 setLike 메서드 추가
	public void setLike(int likeCount) {
		this.likeCount = likeCount;
	}

	@Builder
	public Review(Order order, Store store, Customer customer, String contents) {
		this.order = order;
		this.store = store;
		this.customer = customer;
		this.contents = contents;
		likeCount = 0;
	}

	public void update(String contents){
		this.contents = contents;
	}
}