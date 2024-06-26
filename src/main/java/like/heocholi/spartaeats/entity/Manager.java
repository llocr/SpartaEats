package like.heocholi.spartaeats.entity;

import jakarta.persistence.*;
import like.heocholi.spartaeats.constants.UserRole;
import like.heocholi.spartaeats.constants.UserStatus;
import like.heocholi.spartaeats.dto.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "managers")
@NoArgsConstructor
public class Manager extends Timestamped{
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
