package com.tenco.blog.user;

import com.tenco.blog._core.errors.exception.Exception400;
import com.tenco.blog._core.errors.exception.Exception404;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 *  User 관련 비즈니스 로직
 *  사용자의 프로필 이미지를 파일로 직접 생성하는 코드를 작성
 *  실제 파일을 시스템에 저장하고 삭제하는 작업만 처리할 예정
 *  주의 - 데이터베이스 업데이트는 UserService 에서 처리
 */
@Service
public class ProfileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String uploadProfileImage(MultipartFile multipartFile) throws IOException {
        // 1단계 : 업로드할 디렉토리가 없으면 생성
        createUploadDirectory();

        // 2단계
        String originalFileName = multipartFile.getOriginalFilename();

        // 3단계
        String extension = getFileExtension(originalFileName);

        // 4단계
        String uniqueFileName = generateUniqueFileName(extension);

        // 5단계
        Path filePath = Paths.get(uploadDir, uniqueFileName);

        // 6단계
        multipartFile.transferTo(filePath);

        // 7단계
        return "/uploads/profiles/" + uniqueFileName;
    }

    private String getFileExtension(String originalFilename) {
        if(originalFilename == null || originalFilename.lastIndexOf(".") == -1) {
            return ""; // 확장자가 없으면 빈 문자열을 반환
        }
        // 마지막 점(.) 문자 이후의 문자열을 확장자로 반환
        // 점이 여러개 있을 수 있어서 indexOf는 쓰면 안됨
        // profile.jpg --> lastIndexOf(".") --> 7 반환
        return originalFilename.substring(originalFilename.lastIndexOf("."));
        // profile.jpg <---  .jpg <-- 이부분 추출 됨
    }

    private String generateUniqueFileName(String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMDD_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return timestamp + "_" + uuid + extension;
    }

    private void createUploadDirectory() throws  IOException {
        // window, linux
        Path uploadPath = Paths.get(uploadDir);

        // 디렉토리가 존재 하지 않으면 생성
        // C:/uploads/profiles/ 경로가 없으면
        if(!Files.exists(uploadPath)) {
            // 여러 레벨의 디렉토리를 한번에 생성해 준다.
            Files.createDirectories(uploadPath);
        }
    }

    public void deleteProfileImagePath(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                Path filePath = Paths.get(uploadDir,fileName);
                Files.deleteIfExists(filePath);

            } catch (IOException e) {
                throw new Exception400("프로필 이미지를 삭제하지 못했습니다.");
            }
        }
    }
}
