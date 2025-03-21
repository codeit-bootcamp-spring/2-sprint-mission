package com.sprint.mission.discodeit.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {
    @GetMapping("/index")
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/users")
    public String getUserListPage() {
        return "users";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }
}
