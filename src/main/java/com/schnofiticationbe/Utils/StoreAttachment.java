package com.schnofiticationbe.Utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class StoreAttachment {

    private final String uploadPath = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) throw new IllegalArgumentException("파일이 비어있습니다.");

        String originalName = file.getOriginalFilename();
        if (originalName == null) throw new IllegalArgumentException("파일명이 없습니다.");

        String extension = getExtension(originalName);
        if (!isAllowedExtension(extension)) {
            throw new RuntimeException("허용되지 않은 확장자입니다.");
        }

        try {
            String safeFileName = UUID.randomUUID().toString() + "." + extension;

            File dir = new File(uploadPath);
            if (!dir.exists()) dir.mkdirs();

            File dest = new File(uploadPath + safeFileName);
            file.transferTo(dest);

            return "/uploads/" + safeFileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean isAllowedExtension(String ext) {
        return List.of("jpg", "jpeg", "png", "pdf", "zip", "xlsx").contains(ext);
    }
}
