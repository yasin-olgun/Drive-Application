package com.example.driveApplication.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DiskService {


    @Value("${base.path}")
    private String basePath;

    private List<String> allowedExtensions = Arrays.asList("png", "jpg", "doc", "pdf", "xlsx", "zip", "rar");

    public String write(byte[] data, String extension) throws IOException {
        if (allowedExtensions.contains(extension.toLowerCase())) {
            String filename = UUID.randomUUID() + "." + extension;
            String path = basePath + filename;

            Files.write(Paths.get(path), data, StandardOpenOption.CREATE);

            return filename;
        } else {
            return null;
        }
    }

    public byte[] read(String uniqueName) throws IOException {
        Path path = Paths.get(basePath + uniqueName);
        if (path.toFile().exists()) {
            return Files.readAllBytes(path);
        } else {
            return null;
        }
    }

    public void delete(String uniqueName) throws IOException {
        Path path = Paths.get(basePath + uniqueName);
        if (path.toFile().exists()) {
            Files.delete(path);
        }
    }

}
