package like.heocholi.spartaeats.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PickStoreResponseDto {
    private String storeName;
    private boolean isPick;

}
