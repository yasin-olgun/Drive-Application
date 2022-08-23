package com.example.driveApplication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

import static com.example.driveApplication.service.UserService.SESSION_USER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PageController {


    @GetMapping(value = {"/", "/my-drive","/shared-with-me"})
    public String home(HttpSession session) {
        if (session.getAttribute(SESSION_USER) == null) {
            return "redirect:/login";
        } else {
            return "index";
        }
    }

    @GetMapping(value = {"/login", "/register"})
    public String login() {
        return "index";
    }



    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SESSION_USER);
        log.info("cikis yapildi");
        return "redirect:/login";
    }



}
