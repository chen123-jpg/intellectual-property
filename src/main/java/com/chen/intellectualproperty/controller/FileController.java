package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.service.FileStorageService;
import com.chen.intellectualproperty.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public Result<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.toLowerCase().endsWith(".xml")) {
            return Result.fail("只允许上传 .xml 文件");
        }

        String storedName = fileStorageService.store(file);
        Map<String, String> data = Map.of(
                "filename", storedName,
                "url", "/files/" + storedName
        );
        return Result.success(data);
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename) {
        Resource resource = fileStorageService.loadAsResource(filename);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
