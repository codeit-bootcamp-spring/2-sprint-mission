package com.sprint.mission.discodeit.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/users/{userId}/servers")
public class ServerPageController {
    @GetMapping
    public String getServerPage(@PathVariable UUID userId, Model model) {
        model.addAttribute("userId", userId);
        return "servers";
    }

    @GetMapping("/create")
    public String getCreateServerForm(@PathVariable UUID userId, Model model) {
        model.addAttribute("userId", userId);
        return "create-server";
    }
}
