package like.heocholi.spartaeats.domain.pick.dto;

import like.heocholi.spartaeats.domain.pick.entity.Pick;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PickPageResponseDto {
    private Integer currentPage;
    private Long totalElements;
    private Integer totalPages;
    private Integer size;
    private List<PickResponseDto> pickList;

    public PickPageResponseDto(Integer page, Page<Pick> pickPage) {
        this.currentPage = page;
        this.totalElements = pickPage.getTotalElements();
        this.totalPages = pickPage.getTotalPages();
        this.size = pickPage.getSize();
        this.pickList = pickPage.getContent().stream().map(PickResponseDto::new).toList();


    }
}
