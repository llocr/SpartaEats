package like.heocholi.spartaeats.domain.customer.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import like.heocholi.spartaeats.domain.common.service.S3Service;
import like.heocholi.spartaeats.domain.customer.dto.CustomerResponseDTO;
import like.heocholi.spartaeats.domain.customer.dto.PasswordRequestDTO;
import like.heocholi.spartaeats.domain.customer.dto.ProfileRequestDTO;
import like.heocholi.spartaeats.domain.customer.dto.ProfileResponseDTO;
import like.heocholi.spartaeats.domain.customer.dto.SignupRequestDto;
import like.heocholi.spartaeats.domain.customer.dto.SignupResponseDto;
import like.heocholi.spartaeats.domain.customer.dto.WithdrawRequestDto;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.customer.entity.PasswordHistory;
import like.heocholi.spartaeats.domain.customer.enums.UserStatus;
import like.heocholi.spartaeats.domain.customer.exception.CustomerException;
import like.heocholi.spartaeats.domain.customer.exception.PasswordException;
import like.heocholi.spartaeats.domain.customer.repository.CustomerRepository;
import like.heocholi.spartaeats.domain.customer.repository.PasswordHistoryRepository;
import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.global.exception.FileException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final S3Service s3Service;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 회원가입
     * @param requestDto
     * @return 회원가입 결과
     */
    @Transactional
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        String userId = requestDto.getUserId();
        String password = requestDto.getPassword();

        Optional<Customer> checkUsername = customerRepository.findByUserId(userId);
        if (checkUsername.isPresent()) {
            throw new CustomerException(ErrorType.DUPLICATE_ACCOUNT_ID);
        }

        String encodedPassword = passwordEncoder.encode(password);

        Customer customer = new Customer(requestDto, encodedPassword);
        customerRepository.save(customer);
        insertPasswordHistory(customer);

        return new SignupResponseDto(customer);
    }
    
    /**
     * 회원 탈퇴
     * @param requestDto
     * @param userId
     * @return 탈퇴된 회원Id
     */
    @Transactional
    public String withdrawCustomer(WithdrawRequestDto requestDto, String userId) {
        // 유저 확인
        Customer customer = this.findByUserId(userId);
        // 이미 탈퇴한 회원인지 확인
        if(customer.getUserStatus().equals(UserStatus.DEACTIVATE)){
            throw new CustomerException(ErrorType.DEACTIVATE_USER);
        }
        // 비밀번호 확인
        if(!passwordEncoder.matches(requestDto.getPassword(), customer.getPassword())){
            throw new CustomerException(ErrorType.INVALID_PASSWORD);
        }
        
        customer.withdrawCustomer();
        
        return customer.getUserId();
    }
    
    /**
     * 로그아웃
     * @param userId
     * @return 로그아웃된 회원Id
     */
    @Transactional
    public String logout(String userId) {
        // 유저 확인
        Customer customer = this.findByUserId(userId);
        
        customer.removeRefreshToken();
        
        return customer.getUserId();
    }
    

    /**
     * 프로필 조회
     * @param customer
     * @return 프로필 조회 결과
     */
    public CustomerResponseDTO getCustomerInfo(Customer customer) {
        long likeReviewCount = customerRepository.findLikeReviewCount(customer.getId());
        return new CustomerResponseDTO(customer, likeReviewCount);
    }


    /**
     * 비밀번호 변경
     * @param request
     * @param customer
     * @return 변경된 회원Id
     */
    @Transactional
    public String updatePassword(PasswordRequestDTO request, Customer customer) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), customer.getPassword())) {
            throw new PasswordException(ErrorType.INVALID_PASSWORD);
        }
        
        List<PasswordHistory> passwordHistories = passwordHistoryRepository.findTop3ByCustomerOrderByCreatedAtDesc(customer);

        for (PasswordHistory passwordHistory : passwordHistories) {
            if (passwordEncoder.matches(request.getNewPassword(), passwordHistory.getPassword())) {
                throw new PasswordException(ErrorType.RECENTLY_USED_PASSWORD);
            }
        }
        
        PasswordHistory passwordHistory = PasswordHistory.builder()
                .customer(customer)
                .password(customer.getPassword())
                .build();

        passwordHistoryRepository.save(passwordHistory);
        
        customer.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        customerRepository.save(customer);

        return customer.getUserId();
    }

    /**
     * 프로필 업데이트
     * @param request
     * @param customer
     * @return 업데이트된 프로필
     */
    @Transactional
    public ProfileResponseDTO updateProfile(ProfileRequestDTO request, Customer customer) {
        // 프로필 업데이트
        customer.updateProfile(request);
        customerRepository.save(customer);

        return new ProfileResponseDTO(customer);
    }
    
    /**
     * 프로필 이미지 업로드
     * @param file
     * @param customer
     * @return 업로드된 프로필 이미지
     * @throws IOException
     */
    @Transactional
    public Long uploadProfileImage(MultipartFile file, Customer customer) throws IOException {
        if (file.isEmpty()) {
            throw new FileException(ErrorType.NOT_FOUND_FILE);
        }
        
        String profile = s3Service.upload(file, "profile");
        customer.updateProfileImage(profile);
        customerRepository.save(customer);
        
        return customer.getId();
    }
    
    /**
     * 프로필 이미지 업데이트
     * @param file
     * @param customer
     * @return 업데이트된 프로필 이미지
     * @throws IOException
     */
    @Transactional
    public Long updateProfileImage(MultipartFile file, Customer customer) throws IOException {
        // 이전 프로필 사진 삭제
        if (customer.getProfileImage() == null) {
            throw new FileException(ErrorType.NOT_FOUND_FILE);
        }
        
        s3Service.deleteProfileImage(customer.getProfileImage());
        
        // 새 프로필 사진 업로드
        String newProfileImagePath = s3Service.upload(file, "profile");
        customer.updateProfileImage(newProfileImagePath);
        customerRepository.save(customer);
        return customer.getId();
    }
    
    
    /* util */
    
    /**
     * 비밀번호 히스토리 저장
     * @param customer
     */
    private void insertPasswordHistory(Customer customer) {
        PasswordHistory passwordHistory = PasswordHistory.builder()
                .customer(customer)
                .password(customer.getPassword())
                .build();

        passwordHistoryRepository.save(passwordHistory);
    }
    
    /**
     * 유저 아이디로 회원 조회
     * @param userId
     * @return 회원
     */
    private Customer findByUserId(String userId){
        return customerRepository.findByUserId(userId).orElseThrow(()-> new CustomerException(ErrorType.NOT_FOUND_USER));
    }
}
