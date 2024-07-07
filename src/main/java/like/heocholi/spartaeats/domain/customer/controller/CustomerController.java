package like.heocholi.spartaeats.domain.customer.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import like.heocholi.spartaeats.domain.common.dto.ResponseMessage;
import like.heocholi.spartaeats.domain.customer.dto.CustomerResponseDTO;
import like.heocholi.spartaeats.domain.customer.dto.PasswordRequestDTO;
import like.heocholi.spartaeats.domain.customer.dto.ProfileRequestDTO;
import like.heocholi.spartaeats.domain.customer.dto.ProfileResponseDTO;
import like.heocholi.spartaeats.domain.customer.dto.SignupRequestDto;
import like.heocholi.spartaeats.domain.customer.dto.SignupResponseDto;
import like.heocholi.spartaeats.domain.customer.dto.WithdrawRequestDto;
import like.heocholi.spartaeats.domain.customer.service.CustomerService;
import like.heocholi.spartaeats.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    /**
     * 회원가입
     * @param requestDto
     * @return ResponseMessage<SignupResponseDto>
     *     - statusCode: 201
     *     - message: "회원가입 성공"
     *     - data: 회원가입 결과
     */
    @PostMapping
    public ResponseEntity<ResponseMessage<SignupResponseDto>> signup(@RequestBody @Valid SignupRequestDto requestDto){
        SignupResponseDto responseDto = customerService.signup(requestDto);

        ResponseMessage<SignupResponseDto> responseMessage = ResponseMessage.<SignupResponseDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("회원가입 성공")
                .data(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    /**
     * 회원 탈퇴
     * @param requestDto
     * @param userDetails
     * @return
     *    - statusCode: 200
     *    - message: "회원 탈퇴가 완료되었습니다."
     *    - data: 탈퇴된 회원Id
     */
    @PutMapping("/withdraw")
    public ResponseEntity<ResponseMessage<String>> withdrawCustomer(@RequestBody WithdrawRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userId = userDetails.getUsername();
        String withdrawnUserId = customerService.withdrawCustomer(requestDto, userId);

        ResponseMessage<String> responseMessage = ResponseMessage.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 탈퇴가 완료되었습니다.")
                .data(withdrawnUserId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
    
    /**
     * 로그아웃
     * @param userDetails
     * @return
     *   - statusCode: 200
     *   - message: "로그아웃 성공"
     *   - data: 로그아웃된 회원Id
     */
    @PutMapping("/logout")
    public ResponseEntity<ResponseMessage<String>> logout(@AuthenticationPrincipal UserDetailsImpl userDetails){
        String userId = customerService.logout(userDetails.getUsername());
        
        ResponseMessage<String> responseMessage = ResponseMessage.<String>builder()
            .statusCode(HttpStatus.OK.value())
            .message("로그아웃 성공")
            .data(userId)
            .build();
        
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    
    /**
     * 프로필 조회
     * @param userDetails
     * @return ResponseEntity<ResponseMessage<CustomerResponseDTO>>
     *     - statusCode: 200
     *     - message: "프로필 조회가 완료되었습니다."
     *     - data: 프로필 조회 결과
     */
    @GetMapping
    public ResponseEntity<ResponseMessage<CustomerResponseDTO>> getCustomerInfo (@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CustomerResponseDTO responseDTO =  customerService.getCustomerInfo(userDetails.getCustomer());

        ResponseMessage<CustomerResponseDTO> responseMessage = ResponseMessage.<CustomerResponseDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("프로필 조회가 완료되었습니다.")
                .data(responseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 비밀번호 변경
     * @param request
     * @param userDetails
     * @return ResponseEntity<ResponseMessage<String>>
     *     - statusCode: 200
     *     - message: "비밀번호 변경이 완료되었습니다."
     *     - data: 변경된 회원Id
     */
    @PutMapping("/password")
    public ResponseEntity<ResponseMessage<String>> updatePassword(@RequestBody @Valid PasswordRequestDTO request,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String customerId = customerService.updatePassword(request, userDetails.getCustomer());

        ResponseMessage<String> responseMessage = ResponseMessage.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("비밀번호 변경이 완료되었습니다.")
                .data(customerId)
                .build();


        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 프로필 수정
     * @param request
     * @param userDetails
     * @return ResponseEntity<ResponseMessage<ProfileResponseDTO>>
     *     - statusCode: 200
     *     - message: "프로필 업데이트가 완료되었습니다."
     *     - data: 업데이트된 프로필
     */
    @PutMapping
    public ResponseEntity<ResponseMessage<ProfileResponseDTO>> updateProfile(@RequestBody @Valid ProfileRequestDTO request,
                                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileResponseDTO responseDTO = customerService.updateProfile(request, userDetails.getCustomer());

        ResponseMessage<ProfileResponseDTO> responseMessage = ResponseMessage.<ProfileResponseDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("프로필 업데이트가 완료되었습니다.")
                .data(responseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
    
    /**
     * 프로필 사진 업로드
     * @param file
     * @param userDetails
     * @return ResponseEntity<ResponseMessage<Long>>
     *     - statusCode: 200
     *     - message: "프로필 사진 업로드가 완료되었습니다."
     *     - data: 업로드된 프로필 사진 URL
     * @throws IOException
     */
    @PostMapping("/profile")
    public ResponseEntity<ResponseMessage<Long>> uploadProfile(
        @RequestPart(value = "image") MultipartFile file,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        
        Long responseDTO = customerService.uploadProfileImage(file, userDetails.getCustomer());
        
        ResponseMessage<Long> responseMessage = ResponseMessage.<Long>builder()
            .statusCode(HttpStatus.OK.value())
            .message("프로필 사진 업로드가 완료되었습니다.")
            .data(responseDTO)
            .build();
        
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
    
    /**
     * 프로필 사진 변경
     * @param file
     * @param userDetails
     * @return ResponseEntity<ResponseMessage<Long>>
     *     - statusCode: 200
     *     - message: "프로필 사진 변경이 완료되었습니다."
     *     - data: 변경된 프로필 사진 URL
     * @throws IOException
     */
    @PutMapping("/profile")
    public ResponseEntity<ResponseMessage<Long>> updateProfileImage(
        @RequestPart(value = "image") MultipartFile file,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        
        Long responseDTO = customerService.updateProfileImage(file, userDetails.getCustomer());
        
        ResponseMessage<Long> responseMessage = ResponseMessage.<Long>builder()
            .statusCode(HttpStatus.OK.value())
            .message("프로필 사진 변경이 완료되었습니다.")
            .data(responseDTO)
            .build();
        
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
    
    @DeleteMapping("/profile")
    public ResponseEntity<ResponseMessage<Long>> deleteProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long responseDTO = customerService.deleteProfileImage(userDetails.getCustomer());
        
        ResponseMessage<Long> responseMessage = ResponseMessage.<Long>builder()
            .statusCode(HttpStatus.OK.value())
            .message("프로필 사진 삭제가 완료되었습니다.")
            .data(responseDTO)
            .build();
        
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
