package com.sprint.mission.discodeit.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {
    @GetMapping("/userIndex")
    public String getIndexPage() {
        return "userIndex";
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
