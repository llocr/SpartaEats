package like.heocholi.spartaeats.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
@Table(name ="picks")
public class Pick extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	private boolean isPick;

	@Builder
	public Pick(Customer customer, Store store, boolean isPick) {
		this.customer = customer;
		this.store = store;
		this.isPick = isPick;
	}


	public void update() {
		this.isPick = !this.isPick;
	}
}
