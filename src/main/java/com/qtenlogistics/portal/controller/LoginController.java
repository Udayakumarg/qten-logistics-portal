package com.qtenlogistics.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // maps to login.html
    }

    @GetMapping("/loggedout")
    public String loggedOut() {
        return "loggedout"; // maps to loggedout.html
    }
}
