package like.heocholi.spartaeats.domain.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import like.heocholi.spartaeats.domain.menu.entity.Menu;
import like.heocholi.spartaeats.domain.common.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_menus")
public class OrderMenu extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;
	
	private int count;
	
	private int price;
	
	@Builder
	public OrderMenu(Order order, Menu menu, int count, int price) {
		this.order = order;
		this.menu = menu;
		this.count = count;
		this.price = price;
	}
}
