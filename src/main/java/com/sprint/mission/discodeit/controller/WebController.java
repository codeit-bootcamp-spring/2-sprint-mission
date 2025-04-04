package com.sprint.mission.discodeit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

  @GetMapping("/")
  public String home() {
    return "index";  // templates/index.html 반환
  }
}