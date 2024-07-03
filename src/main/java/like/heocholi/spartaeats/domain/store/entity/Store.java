package like.heocholi.spartaeats.domain.store.entity;

import java.util.List;

import jakarta.persistence.*;
import like.heocholi.spartaeats.domain.store.enums.RestaurantType;
import like.heocholi.spartaeats.domain.manager.entity.Manager;
import like.heocholi.spartaeats.domain.menu.entity.Menu;
import like.heocholi.spartaeats.domain.order.entity.Order;
import like.heocholi.spartaeats.domain.pick.entity.Pick;
import like.heocholi.spartaeats.domain.review.entity.Review;
import like.heocholi.spartaeats.domain.common.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "stores")
public class Store extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id")
	private Manager manager;
	
	private String address;
	
	@Enumerated(EnumType.STRING)
	private RestaurantType type;

	@OneToMany(mappedBy = "store", cascade = CascadeType.PERSIST)
	private List<Menu> menuList;

	@OneToMany(mappedBy = "store", cascade = CascadeType.PERSIST)
	private List<Order> orders;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pick> pickList;

	@OneToMany(mappedBy = "store", cascade = CascadeType.PERSIST)
	private List<Review> reviews;
	
	@Builder
	public Store(String name, Manager manager, String address, RestaurantType type) {
		this.name = name;
		this.manager = manager;
		this.address = address;
		this.type = type;
	}
}