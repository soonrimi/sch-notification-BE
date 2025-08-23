package conconccc.schnofiticationbe.Utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class StoreAttachment {

    // 악성파일 거르기
    private boolean isAllowedExtension(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".png") ||
                lower.endsWith(".pdf") || lower.endsWith(".docx") ||
                lower.endsWith(".hwp") || lower.endsWith(".zip") ||
                lower.endsWith(".xlsx") || lower.endsWith(".jpeg");
    }

    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어있습니다.");
        }

        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.trim().isEmpty()) {
                throw new IllegalArgumentException("원본 파일명이 없습니다.");
            }

            if (!isAllowedExtension(originalName)) {
                throw new RuntimeException("허용되지 않은 파일 형식입니다: " + originalName);
            }

            // 프로젝트 루트 기준 절대 경로
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

            // uploads 폴더 만약 없으면 자동 추가
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + originalName;
            File dest = new File(uploadDir + fileName);
            file.transferTo(dest);

            // DB에는 URL 경로 저장 (WebConfig와 매핑되는 /uploads/**)
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        }
    }
}
