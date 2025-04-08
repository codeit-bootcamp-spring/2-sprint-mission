package com.sprint.mission.discodeit.adapter.inbound.user;

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
    return "user-list";
  }

//    @GetMapping("/register")
//    public String getRegisterPage() {
//        return "register";
//    }
//
//    @GetMapping("/login")
//    public String getLoginPage() {
//        return "login";
//    }
}
