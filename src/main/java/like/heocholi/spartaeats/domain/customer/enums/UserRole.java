package like.heocholi.spartaeats.domain.customer.enums;

import lombok.Getter;

@Getter
public enum UserRole {
	ROLE_CUSTOMER("고객"),
	ROLE_MANAGER("점주"),
	ROLE_ADMIN("admin");
	
	private final String status;
	
	UserRole(String status) {
		this.status = status;
	}
}
