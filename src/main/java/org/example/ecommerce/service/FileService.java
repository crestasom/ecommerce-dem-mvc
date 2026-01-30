package org.example.ecommerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

@Service
public class FileService {

    @Value("${upload.path:uploads}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String fileName = file.getOriginalFilename() + "_" + System.currentTimeMillis();
            Path targetLocation = Paths.get(uploadPath).resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Return only the path part used by the web handler
            return "uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public String saveBase64File(String base64Content, String fileName) {
        if (base64Content == null || base64Content.isEmpty()) {
            return null;
        }

        try {
            // Remove data:image/png;base64, prefix if present
            if (base64Content.contains(",")) {
                base64Content = base64Content.split(",")[1];
            }

            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
            String finalFileName = fileName + "_" + System.currentTimeMillis();
            Path targetLocation = Paths.get(uploadPath).resolve(finalFileName);

            Files.write(targetLocation, decodedBytes);
            return "uploads/" + finalFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store base64 file", e);
        }
    }

    public String readFileAsBase64(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            return null;
        }

        try {
            // relativePath is like "uploads/filename"
            String fileName = relativePath.substring(relativePath.lastIndexOf("/") + 1);
            Path finalPath = Paths.get(uploadPath).resolve(fileName);

            if (Files.exists(finalPath)) {
                byte[] bytes = Files.readAllBytes(finalPath);
                return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
