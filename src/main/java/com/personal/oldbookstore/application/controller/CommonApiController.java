package com.personal.oldbookstore.application.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class CommonApiController {

    @Value("${file.dir}")
    private String imgUploadPath;

    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> fileUrl(@PathVariable String imageName) throws IOException {
        Resource resource = new FileSystemResource(imgUploadPath + File.separator + imageName);

        if (!resource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        Path savePath = Paths.get(imgUploadPath + File.separator + imageName).toAbsolutePath();
        headers.add("Content-Type", Files.probeContentType(savePath));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
