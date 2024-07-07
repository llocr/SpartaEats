package like.heocholi.spartaeats.domain.customer.entity;

import jakarta.persistence.*;
import like.heocholi.spartaeats.domain.customer.enums.UserRole;
import like.heocholi.spartaeats.domain.customer.enums.UserStatus;
import like.heocholi.spartaeats.domain.customer.dto.ProfileRequestDTO;
import like.heocholi.spartaeats.domain.customer.dto.SignupRequestDto;
import like.heocholi.spartaeats.domain.pick.entity.Pick;
import like.heocholi.spartaeats.domain.common.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "customers")
@NoArgsConstructor
public class Customer extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userId;

	private String name;

	private String password;
	
	private String profileImage;
	
	private String address;

	private String refreshToken;

	private String bio; // 한 줄 소개 필드

	@Enumerated(value = EnumType.STRING)
	private UserStatus userStatus;

	@Enumerated(value = EnumType.STRING)
	private UserRole role;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pick> pickList;
	
	public Customer(SignupRequestDto requestDto, String encodedPassword) {
		this.userId = requestDto.getUserId();
		this.password = encodedPassword;
		this.name = requestDto.getName();
		this.address = requestDto.getAddress();
		this.userStatus = UserStatus.ACTIVE;
		this.role = UserRole.ROLE_CUSTOMER;
	}
	
	@Builder
	public Customer(String userId, String name, String password, String address, String refreshToken, String bio, UserStatus userStatus, UserRole role) {
		this.userId = userId;
		this.name = name;
		this.password = password;
		this.address = address;
		this.refreshToken = refreshToken;
		this.bio = bio;
		this.userStatus = userStatus;
		this.role = role;
	}

	public void saveRefreshToken(String refreshToken){
		this.refreshToken = refreshToken;
	}

	public boolean validateRefreshToken(String refreshToken){
		return this.refreshToken != null && this.refreshToken.equals(refreshToken);
	}

	// 프로필 업데이트 메서드
	public void updateProfile(ProfileRequestDTO requestDTO) {
		this.name = requestDTO.getName() != null ? requestDTO.getName() : name;
		this.bio = requestDTO.getBio() != null ? requestDTO.getBio() : bio;
	}
	
	// 프로필 사진 업데이트 메서드
	public void updateProfileImage(String imageUrl) {
		this.profileImage = imageUrl;
	}

	// 비밀번호 업데이트 메서드
	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

	public void withdrawCustomer() {
		this.userStatus = UserStatus.DEACTIVATE;
	}

	public void removeRefreshToken() {
		this.refreshToken = "";
	}
}
