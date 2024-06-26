package like.heocholi.spartaeats.domain.manager.entity;

import jakarta.persistence.*;
import like.heocholi.spartaeats.domain.customer.enums.UserRole;
import like.heocholi.spartaeats.domain.customer.enums.UserStatus;
import like.heocholi.spartaeats.domain.customer.dto.SignupRequestDto;
import like.heocholi.spartaeats.domain.store.entity.Store;
import like.heocholi.spartaeats.domain.common.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "managers")
@NoArgsConstructor
public class Manager extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userId;
	
	private String password;
	
	private String refreshToken;

	@Enumerated(value = EnumType.STRING)
	private UserStatus userStatus;

	@Enumerated(value = EnumType.STRING)
	private UserRole role;
	
	@OneToMany(mappedBy = "manager", cascade = CascadeType.PERSIST)
	private List<Store> storeList;

	public Manager(SignupRequestDto requestDto, String encodedPassword) {
		this.userId = requestDto.getUserId();
		this.password = encodedPassword;
		this.userStatus = UserStatus.ACTIVE;
		this.role = UserRole.ROLE_CUSTOMER;
	}
}
