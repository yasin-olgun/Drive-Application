package com.example.driveApplication.service;

import com.example.driveApplication.model.File;
import com.example.driveApplication.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final DiskService diskService;

    public void saveFile(File file) {
        if (file.getId() == null) {// new file
            file.setId(UUID.randomUUID().toString());
            fileRepository.save(file);
        } else {
            fileRepository.save(file); // update
        }

    }

    public File uploadFile(MultipartFile file,String ownerId) throws IOException {
        File f = new File();
        f.setOwnerId(ownerId);
        f.setId(UUID.randomUUID().toString());
        f.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
        f.setSize((file.getSize()/1024));

        String fileName = diskService.write(file.getBytes(), f.getExtension());
        f.setName(file.getOriginalFilename());
        f.setUniqueName(fileName);
        fileRepository.save(f);
        return f;

    }


    public File findById(String id) {
        return fileRepository.findById(id)
                .orElse(null);
    }

    public void deleteFileById(String uniqueName) throws IOException {
        fileRepository.deleteById(uniqueName);
        diskService.delete(uniqueName);

    }

    public byte[] downloadFile(String uniqueName) throws IOException {
      //  Optional<File> f = fileRepository.findById(FilenameUtils.getBaseName(uniqueName));
        byte[] file = diskService.read(uniqueName);
        return file;

    }

    public void removeFile(String name) throws IOException {
        diskService.delete(name);

    }

}
