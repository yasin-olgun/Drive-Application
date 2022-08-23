package com.example.driveApplication.controller;

import com.example.driveApplication.dto.AuthenticationResponse;
import com.example.driveApplication.dto.AuthenticatonRequest;
import com.example.driveApplication.model.File;
import com.example.driveApplication.model.User;
import com.example.driveApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.example.driveApplication.service.UserService.SESSION_USER;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/file")
    public List<File> myFiles(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        return userService.getMyFiles(user.getId());
    }

    @GetMapping("/shared-file")
    public List<File> sharedFiles(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        return userService.getSharedFiles(user.getId());
    }

    @PostMapping("/login")
    @ResponseBody
    public AuthenticationResponse login(@RequestBody AuthenticatonRequest request, HttpSession session) {

        AuthenticationResponse response = userService.login(request.getUsername(), request.getPassword());
        if (response.getCode() == 0) {
            session.setAttribute(SESSION_USER, response.getUser());
        }
        return response;
    }

    @PostMapping("/upload")
    public File uploadFile(@RequestPart MultipartFile file, HttpSession session) throws IOException {
        User user = (User) session.getAttribute(SESSION_USER);
        File f = userService.uploadFile(file, user);
        return f;
    }

    @PostMapping("/share")
    public void share(@RequestParam("fileId") String fileId, @RequestParam("username") String username, HttpSession session) throws IOException {
        User user = (User) session.getAttribute(SESSION_USER);
        userService.shareFile(fileId, username, user.getId());
    }

    @PostMapping("/unshare")
    public void unShare(@RequestParam("fileId") String fileId, @RequestParam("username") String username, HttpSession session) throws IOException {
        User user = (User) session.getAttribute(SESSION_USER);
        userService.unShareFile(fileId, username, user.getId());
    }


    @GetMapping("/download/{uniqueName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String uniqueName) { // fileName with ext
        try {

            HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); //todo mediatype kontrol edilmeli
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); //
            headers.setContentDisposition(ContentDisposition.attachment().build());
            return new ResponseEntity(userService.downloadFile(uniqueName), headers, HttpStatus.OK);
        } catch (IOException e) {
            return null;
        }

    }


    @DeleteMapping("{fileId}")
    public File deleteFile(@PathVariable String fileId, HttpSession session) throws IOException {
        User user = (User) session.getAttribute(SESSION_USER);
        File f = userService.deleteFile(fileId, user.getId());
        return f;
    }

    @GetMapping()
    public User get(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        return user;
    }

    @PostMapping()
    public List<User> getUsersByIdList(@RequestBody List<String> idList) {
        return userService.findUsersByIdList(idList);
    }


    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok().body(userService.saveUser(user));
    }


}


