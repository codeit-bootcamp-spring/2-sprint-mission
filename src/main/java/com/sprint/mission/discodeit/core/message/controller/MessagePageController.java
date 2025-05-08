package com.sprint.mission.discodeit.core.message.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/users/{userId}/servers/{serverId}/channels/{channelId}")
public class MessagePageController {

  @GetMapping("/messages")
  public String getMessagePage(
      @PathVariable UUID userId,
      @PathVariable UUID serverId,
      @PathVariable UUID channelId,
      Model model) {

    model.addAttribute("userId", userId);
    model.addAttribute("serverId", serverId);
    model.addAttribute("channelId", channelId);
    return "messages";
  }

}
