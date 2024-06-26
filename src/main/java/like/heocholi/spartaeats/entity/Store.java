package like.heocholi.spartaeats.entity;

import java.util.List;

import jakarta.persistence.*;
import like.heocholi.spartaeats.constants.RestaurantType;
import lombok.Getter;

@Getter
@Entity
@Table(name = "stores")
public class Store extends Timestamped{
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
}