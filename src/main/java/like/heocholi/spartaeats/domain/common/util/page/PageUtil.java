package like.heocholi.spartaeats.domain.common.util.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.global.exception.PageException;

public class PageUtil {
	public static final int DEFAULT_PAGE_SIZE = 5;
	
	public static Pageable createPageable(Integer page, Sort sort) {
		if (page < 1) {
			throw new PageException(ErrorType.INVALID_PAGE);
		}
		
		return PageRequest.of(page - 1, DEFAULT_PAGE_SIZE, sort);
	}
	
	public static void checkValidatePage(Integer page, Page<?> pageList) {
		if (pageList.getTotalElements() == 0) {
			throw new PageException(ErrorType.NOT_FOUND_PAGE);
		}
		
		if (page > pageList.getTotalPages() || page < 1) {
			throw new PageException(ErrorType.INVALID_PAGE);
		}
	}
}
