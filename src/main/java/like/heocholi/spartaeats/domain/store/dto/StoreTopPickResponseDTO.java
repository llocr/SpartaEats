package like.heocholi.spartaeats.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreTopPickResponseDTO {
	private String name;
	private String address;
	private long pickCount;
}
