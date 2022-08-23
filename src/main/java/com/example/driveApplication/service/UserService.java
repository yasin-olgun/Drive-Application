package com.example.driveApplication.service;


import com.example.driveApplication.dto.AuthenticationResponse;
import com.example.driveApplication.model.File;
import com.example.driveApplication.model.User;
import com.example.driveApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    public static final String SESSION_USER = "user";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FileService fileService;


    public User saveUser(User user) {
        boolean alreadyTaken = userRepository.findByUsername(user.getUsername()).isPresent();

        if (!alreadyTaken) {
            // boyle username'e sahip bir kullanici yoksa
            user.setId(UUID.randomUUID().toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return user;
        }
        if (user.getId() != null) {
            userRepository.save(user);
        }
        return null;
    }

    public List<User> findUsersByIdList(List<String> idList) {
        return (List<User>) userRepository.findAllById(idList);
    }


    public User findById(String id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public void shareFile(String fileId, String userName, String sharerId) {
        // dosya kullanici ile paylasildi, user
        Optional<User> sharerUser = userRepository.findById(sharerId);
        Optional<User> user = userRepository.findByUsername(userName);
        File file = fileService.findById(fileId);
        List<String> sharedWithUser = user.get().getSharedWithMeIds();
        if (!sharedWithUser.contains(fileId)) {
            sharedWithUser.add(fileId);
        }
        user.get().setSharedWithMeIds(sharedWithUser); // paylasilan kullanicinin benimle paylasilanlar listesine eklendi
        userRepository.save(user.get());
        List<String> sharedUsers = file.getSharedUserIds(); // file icerisine o file'a erisebilecek kisilere eklendi
        sharedUsers.add(user.get().getId());
        file.setSharedUserIds(sharedUsers);
        fileService.saveFile(file);
        HashMap map = sharerUser.get().getSharedFiles();
        List<String> sharedFileUsers = (List<String>) map.get(fileId);
        if (sharedFileUsers != null && !sharedFileUsers.contains(user.get().getId())) {
            sharedFileUsers.add(user.get().getId());
            map.put(fileId, sharedFileUsers); //todo hatali olabilir, hashmap'e ekleme yapmadan once eski key value silinip tekrar eklenebilir
            sharerUser.get().setSharedFiles(map);
            userRepository.save(user.get());
        }

    }

    public void unShareFile(String fileId, String userName, String sharerId) {
        Optional<User> sharerUser = userRepository.findById(sharerId);
        Optional<User> user = userRepository.findByUsername(userName);
        File file = fileService.findById(fileId);
        List<String> sharedWithUser = user.get().getSharedWithMeIds();
        sharedWithUser.remove(fileId);
        user.get().setSharedWithMeIds(sharedWithUser);
        userRepository.save(user.get());
        List<String> sharedUsers = file.getSharedUserIds();
        sharedUsers.remove(user.get().getId());
        file.setSharedUserIds(sharedUsers);
        fileService.saveFile(file);

        HashMap map = sharerUser.get().getSharedFiles();
        List<String> sharedFileUsers = (List<String>) map.get(fileId);
        if (sharedFileUsers != null && sharedFileUsers.contains(userName)) {
            sharedFileUsers.remove(userName);
            map.put(fileId, sharedFileUsers); //todo hatali olabilir, hashmap'e ekleme yapmadan once eski key value silinip tekrar eklenebilir
            sharerUser.get().setSharedFiles(map);
            userRepository.save(user.get());
        }


    }


    public File uploadFile(MultipartFile file, User user) throws IOException {
        File f = fileService.uploadFile(file, user.getId());
        String fileId = f.getId();
        Optional<User> u = userRepository.findById(user.getId());
        List<String> userFiles = u.get().getFileIds();
        userFiles.add(fileId);
        user.setFileIds(userFiles);
        userRepository.save(user);
        return f;
    }

    public byte[] downloadFile(String uniqueName) throws IOException {
        return fileService.downloadFile(uniqueName);
    }

    public File deleteFile(String fileId, String userId) throws IOException {
        Optional<User> user = userRepository.findById(userId);
        File f = fileService.findById(fileId);
        if (user.get().getFileIds().contains(fileId)) {
            // dosyayi yukleyen kullanici ise (owner) dosya tamamen silinecek
            fileService.deleteFileById(f.getId());
            List<String> userFileIds = user.get().getFileIds();
            userFileIds.remove(fileId);
            user.get().setFileIds(userFileIds);
            userRepository.save(user.get());
            fileService.removeFile(f.getUniqueName());
            return f;
        } else {
            // bu dosya kullanici ile share edilmis, sadece kullanicinin shared listinden silmek yeterli
            List<String> sharedWithMe = user.get().getSharedWithMeIds();
            if (sharedWithMe.contains(fileId)) {
                sharedWithMe.remove(fileId);
                user.get().setSharedWithMeIds(sharedWithMe);
                userRepository.save(user.get());
                return f;
            }
        }
        return null;
    }

    public List<File> getMyFiles(String userId) {
        List<File> files = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);
        List<String> userFiles = user.get().getFileIds();
        for (String id : userFiles) {
            files.add(fileService.findById(id));
        }
        return files;
    }

    public List<File> getSharedFiles(String userId) {
        List<File> files = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);
        List<String> userFiles = user.get().getSharedWithMeIds();
        for (String id : userFiles) {
            files.add(fileService.findById(id));
        }
        return files;
    }


    public AuthenticationResponse login(String username, String password) {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return new AuthenticationResponse()
                    .setCode(11);
        } else {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return new AuthenticationResponse()
                        .setCode(0)
                        .setUser(user);

                //todo set.User(user) yapmadim, etkisi olacak mi?
            } else {
                return new AuthenticationResponse()
                        .setCode(10);
            }
        }

    }


}
