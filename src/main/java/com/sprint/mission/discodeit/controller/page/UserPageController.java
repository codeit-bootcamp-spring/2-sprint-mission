package com.sprint.mission.discodeit.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {
    @GetMapping("/users")
    public String getUserListPage() {
        return "users";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";  // templates/legacy_register.html 파일을 렌더링
    }

}
