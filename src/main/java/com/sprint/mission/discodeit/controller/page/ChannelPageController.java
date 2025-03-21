package com.sprint.mission.discodeit.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/users/{userId}/servers/{serverId}")
public class ChannelPageController {
    @GetMapping
    public String getChannelPage(@PathVariable UUID userId, @PathVariable UUID serverId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("serverId", serverId);
        return "channels";
    }

    @GetMapping("/create")
    public String getCreateChannelForm(@PathVariable UUID userId, @PathVariable UUID serverId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("serverId", serverId);
        return "create-channel";
    }
}
