package com.example.butim.domain.user.service;

import com.example.butim.domain.auth.service.PhoneVerificationService;
import com.example.butim.domain.user.dto.request.UpdateUserRequest;
import com.example.butim.domain.user.dto.response.UserMeResponse;
import com.example.butim.domain.user.entity.User;
import com.example.butim.domain.user.repository.UserRepository;
import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneVerificationService phoneVerificationService;

    @Transactional(readOnly = true)
    public UserMeResponse getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserMeResponse(user.getName(), user.getEmail(), user.getPhoneNumber());
    }

    @Transactional
    public void updateMe(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (request.getEmail() != null && userRepository.existsByEmailAndIdNot(request.getEmail(), userId)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            if (userRepository.existsByPhoneNumberAndIdNot(request.getPhoneNumber(), userId)) {
                throw new CustomException(ErrorCode.DUPLICATE_PHONE);
            }
            phoneVerificationService.checkVerified(request.getPhoneNumber());
        }

        String encodedPassword = null;
        if (request.getPassword() != null) {
            if (!request.getPassword().equals(request.getPasswordConfirm())) {
                throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
            }
            encodedPassword = passwordEncoder.encode(request.getPassword());
        }

        user.update(
                request.getName(),
                request.getEmail(),
                encodedPassword,
                request.getPhoneNumber(),
                request.getTermsAgreed(),
                request.getPushAlarmAgreed()
        );

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            phoneVerificationService.deleteVerified(request.getPhoneNumber());
        }
    }
}