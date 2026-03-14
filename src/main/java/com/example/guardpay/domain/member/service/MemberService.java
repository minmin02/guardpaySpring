package com.example.guardpay.domain.member.service;

import com.example.guardpay.domain.member.converter.MemberConverter;
import com.example.guardpay.domain.member.dto.request.MemberDto;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.enums.FileErrorCode;
import com.example.guardpay.domain.member.enums.MemberErrorCode;
import com.example.guardpay.domain.member.exception.FileException;
import com.example.guardpay.domain.member.exception.MemberException;
import com.example.guardpay.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// MemberService를 빈 객체로 등록
@Service
//final이 붙은 필드들을 생성자를 자동으로 만들어줌
@RequiredArgsConstructor
public class MemberService {

    // 의존성 주입
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberConverter memberConverter;
    private final FileStorageService fileStorageService;

    /**
     * 마이페이지 프로필 수정 (닉네임, 비밀번호)
     */
    @Transactional
    public MemberDto.UpdateProfileResponse updateProfile(Long memberId, MemberDto.UpdateProfileRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 🔹 닉네임 변경 로직
        if (request.getNickname() != null && !request.getNickname().trim().isEmpty()) {
            member.updateNickname(request.getNickname());
        }

        // 🔹 비밀번호 변경 로직
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            if (request.getCurrentPassword() == null || request.getCurrentPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("현재 비밀번호를 입력해주세요.");
            }
            if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
            }

            String encodedPassword = passwordEncoder.encode(request.getPassword());
            member.updatePassword(encodedPassword);
        }

        return memberConverter.toUpdateProfileResponse(member, "프로필이 성공적으로 수정되었습니다.");
    }


    @Transactional(readOnly = true)
    public MemberDto.MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return memberConverter.toMemberInfoResponse(member);
    }


    /**
     * 프로필 이미지 업데이트
     */
    @Transactional
    public MemberDto.UpdateProfileImageResponse updateProfileImage(Long memberId, MultipartFile profileImage) {
        // 1. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 2. 파일 검증
        validateImageFile(profileImage);

        // 3. 기존 이미지 삭제
        String oldImageUrl = member.getProfileImageUrl();
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            fileStorageService.deleteFile(oldImageUrl);
        }

        // 4. 새 이미지 업로드
        String newImageUrl = fileStorageService.uploadFile(profileImage, "profiles");

        // 5. 프로필 이미지 업데이트
        member.updateProfileImage(newImageUrl);

        // 6. 응답 생성
        return memberConverter.toUpdateProfileImageResponse(member, "프로필 이미지가 성공적으로 수정되었습니다.");
    }

    /**
     * 프로필 이미지 삭제
     */
    @Transactional
    public void deleteProfileImage(Long memberId) {
        // 1. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 2. 기존 이미지 파일 삭제
        String oldImageUrl = member.getProfileImageUrl();
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            fileStorageService.deleteFile(oldImageUrl);
        }

        // 3. 프로필 이미지를 null로 설정
        member.updateProfileImage(null);
    }

    /**
     * 등급 조회
     */
    @Transactional
    public Map<String, String> getGrade(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Map<String, String> response = new HashMap<>();
        response.put("grade", member.getGrade().name());

        return response;
    }

    /**
     * 이미지 파일 검증
     */
    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileException(FileErrorCode.FILE_EMPTY);
        }

        // 파일 크기 검증 (5MB)
        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new FileException(FileErrorCode.FILE_SIZE_EXCEEDED);
        }

        // 파일 타입 검증
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new FileException(FileErrorCode.FILE_TYPE_INVALID);
        }

        // 허용된 확장자 검증
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new FileException(FileErrorCode.FILE_NAME_INVALID);
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!List.of("jpg", "jpeg", "png", "gif", "webp").contains(extension)) {
            throw new FileException(FileErrorCode.FILE_EXTENSION_NOT_SUPPORTED);
        }
    }



}