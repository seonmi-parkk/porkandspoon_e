package kr.co.porkandspoon.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${upload.path}") String paths;
    @Value("${uploadTem.path}") String tem_path;

    // filePond 이미지 미리보기
    @GetMapping("/filepond/{new_filename}")
    public ResponseEntity<Resource> filePondPreview(@PathVariable String new_filename) {
        Path path = Paths.get(paths + "/" + new_filename);
        Resource resource = new FileSystemResource(path.toFile());

        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (contentType == null) contentType = "application/octet-stream"; // fallback

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
