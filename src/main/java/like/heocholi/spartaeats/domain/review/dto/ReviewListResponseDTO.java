package like.heocholi.spartaeats.domain.review.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import like.heocholi.spartaeats.domain.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewListResponseDTO {
	private Integer currentPage;
	private Long totalElements;
	private Integer totalPages;
	private Integer size;
	private List<ReviewResponseDto> reviewList;
	
	public ReviewListResponseDTO(Integer currentPage, Page<Review> reviewPage) {
		this.currentPage = currentPage;
		this.totalElements = reviewPage.getTotalElements();
		this.totalPages = reviewPage.getTotalPages();
		this.size = reviewPage.getSize();
		this.reviewList = reviewPage.getContent().stream().map(ReviewResponseDto::new).toList();
	}
}
