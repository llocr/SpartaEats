package like.heocholi.spartaeats.domain.order.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import like.heocholi.spartaeats.domain.order.enums.OrderState;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.store.entity.Store;
import like.heocholi.spartaeats.domain.common.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	private String address;
	
	@Column(columnDefinition = "varchar(30)")
	@Enumerated(EnumType.STRING)
	private OrderState state;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
	private List<OrderMenu> orderMenuList;
	
	private int totalPrice;
	
	public Order(Store store,Customer customer) {
		this.state = OrderState.NONE;
		this.store = store;
		this.customer = customer;
		this.address = customer.getAddress();
	}
	
	public void updateOrder(List<OrderMenu> orderMenuList, int totalPrice) {
		this.state = OrderState.ORDER_WAITING;
		this.orderMenuList = orderMenuList;
		this.totalPrice = totalPrice;
	}
	
	public void cancelOrder() {
		this.state = OrderState.ORDER_CANCELLED;
	}
}
