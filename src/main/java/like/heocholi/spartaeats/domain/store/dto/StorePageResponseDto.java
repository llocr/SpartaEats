package like.heocholi.spartaeats.domain.store.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import like.heocholi.spartaeats.domain.store.entity.Store;
import lombok.Getter;

@Getter
public class StorePageResponseDto {
    private Integer currentPage;
    private Long totalElements;
    private Integer totalPages;
    private Integer size;
    private List<StoreListResponseDto> storeList;

    public StorePageResponseDto(Integer currentPage, Page<Store> storePageList) {
        this.currentPage = currentPage;
        this.totalElements = storePageList.getTotalElements();
        this.totalPages = storePageList.getTotalPages();
        this.size = storePageList.getSize();
        this.storeList = storePageList.getContent().stream().map(StoreListResponseDto::new).toList();
    }
}
