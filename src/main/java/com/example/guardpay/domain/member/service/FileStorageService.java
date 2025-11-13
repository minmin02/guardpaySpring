package com.example.guardpay.domain.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    @Value("${file.base-url:http://localhost:8080/files}")
    private String baseUrl;

    /**
     * 파일 업로드
     */
    public String uploadFile(MultipartFile file, String directory) {
        try {
            // 파일명 생성 (UUID + 원본 확장자)
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;

            // 디렉토리 생성
            Path uploadPath = Paths.get(uploadDir, directory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 저장
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // URL 반환
            String fileUrl = baseUrl + "/" + directory + "/" + fileName;
            log.info("File uploaded successfully: {}", fileUrl);

            return fileUrl;

        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }

    /**
     * 파일 삭제
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            // URL에서 파일 경로 추출
            String relativePath = fileUrl.substring(baseUrl.length() + 1);
            Path filePath = Paths.get(uploadDir, relativePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("File deleted successfully: {}", fileUrl);
            }
        } catch (IOException e) {
            log.error("Failed to delete file: {}", fileUrl, e);
            // 파일 삭제 실패는 치명적이지 않으므로 예외를 던지지 않음
        }
    }
}