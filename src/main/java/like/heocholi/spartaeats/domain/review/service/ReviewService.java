package like.heocholi.spartaeats.domain.review.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import like.heocholi.spartaeats.domain.common.util.page.PageUtil;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.order.entity.Order;
import like.heocholi.spartaeats.domain.order.service.OrderService;
import like.heocholi.spartaeats.domain.review.dto.ReviewAddRequestDto;
import like.heocholi.spartaeats.domain.review.dto.ReviewListResponseDTO;
import like.heocholi.spartaeats.domain.review.dto.ReviewResponseDto;
import like.heocholi.spartaeats.domain.review.dto.ReviewSearchCond;
import like.heocholi.spartaeats.domain.review.dto.ReviewUpdateRequestDto;
import like.heocholi.spartaeats.domain.review.entity.Review;
import like.heocholi.spartaeats.domain.review.exception.ReviewException;
import like.heocholi.spartaeats.domain.review.repository.ReviewRepository;
import like.heocholi.spartaeats.domain.store.entity.Store;
import like.heocholi.spartaeats.domain.store.service.StoreService;
import like.heocholi.spartaeats.global.exception.ErrorType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderService orderService;
    private final StoreService storeService;
    
    /**
     * 리뷰 전체 조회
     * @param storeId
     * @return 리뷰 리스트
     */
    public List<ReviewResponseDto> getReviews(Long storeId) {
        Store store = storeService.findStoreById(storeId);
        List<ReviewResponseDto> reviewList = store.getReviews().stream().map(ReviewResponseDto::new).toList();

        return reviewList;
    }

    /**
     * 리뷰 단건 조회
     * @param storeId
     * @param reviewId
     * @return 리뷰 정보
     */
    public ReviewResponseDto getReview(Long storeId, Long reviewId) {
        Review review = reviewRepository.findByStoreIdAndId(storeId, reviewId).orElseThrow(
                () -> new ReviewException(ErrorType.NOT_FOUND_REVIEW)
        );

        return new ReviewResponseDto(review);
    }


    /**
     * 리뷰 추가
     * @param orderId
     * @param requestDto
     * @param customer
     * @return 리뷰 정보
     */
    @Transactional
    public ReviewResponseDto addReview(Long orderId, ReviewAddRequestDto requestDto, Customer customer) {
        Order order = orderService.findOrderById(orderId);
        
        if (!customer.getId().equals(order.getCustomer().getId())) {
            throw new ReviewException(ErrorType.INVALID_ORDER_CUSTOMER);
        }

        Review review = Review.builder()
                .order(order)
                .store(order.getStore())
                .customer(order.getCustomer())
                .contents(requestDto.getContents())
                .build();

        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }
    
    /**
     * 리뷰 수정
     * @param reviewId
     * @param requestDto
     * @param customer
     * @return 리뷰 정보
     */
    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto requestDto, Customer customer) {
        Review review = findReviewById(reviewId);
        checkValidateCustomer(customer, review);

        review.update(requestDto.getContents());

        return new ReviewResponseDto(review);
    }

    /**
     * 리뷰 삭제
     * @param reviewId
     * @param customer
     * @return 리뷰 ID
     */
    @Transactional
    public Long deleteReview(Long reviewId, Customer customer) {
        Review review = findReviewById(reviewId);
        checkValidateCustomer(customer, review);

        reviewRepository.delete(review);

        return review.getId();
    }
    
    /**
     * 좋아요한 리뷰 조회
     * @param page
     * @param customer
     * @return 리뷰 리스트
     */
    public ReviewListResponseDTO likeReviews(Integer page, Customer customer) {
        Pageable pageable = PageUtil.createPageable(page, Sort.by("createdAt").descending());
        Page<Review> reviewPage = reviewRepository.findLikeReview(customer.getId(), pageable);
        PageUtil.checkValidatePage(page, reviewPage);
        
        return new ReviewListResponseDTO(page, reviewPage);
    }
    
    /**
     * 찜한 가게의 리뷰 조회
     * @param page
     * @param sort
     * @param customer
     * @return 리뷰 리스트
     */
    public ReviewListResponseDTO pickReviews(Integer page, String sort, ReviewSearchCond reviewSearchCond, Customer customer) {
        Pageable pageable = PageUtil.createPageable(page, Sort.by(sort));
        Page<Review> reviewPage = reviewRepository.findPickReview(reviewSearchCond, customer.getId(), pageable);
        PageUtil.checkValidatePage(page, reviewPage);
        
        return new ReviewListResponseDTO(page, reviewPage);
    }
    
    /* Util */
    
    /**
     * 리뷰 작성자 확인
     * @param customer\\
     * @param review
     */
    private static void checkValidateCustomer(Customer customer, Review review) {
        if (!customer.getId().equals(review.getCustomer().getId())) {
            throw new ReviewException(ErrorType.INVALID_ORDER_CUSTOMER);
        }
    }
    
    /**
     * 리뷰 조회
     * @param reviewId
     * @return 리뷰
     */
    public Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ReviewException(ErrorType.NOT_FOUND_REVIEW));
    }
}
