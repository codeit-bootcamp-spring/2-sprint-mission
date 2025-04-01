package com.sprint.mission.discodeit.adapter.inbound.channel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class ChannelPageController {
    @GetMapping("/channel-index")
    public String getChannelPage(@RequestParam UUID serverId, Model model) {
        model.addAttribute("serverId", serverId);
        return "channel-index";
    }

    @GetMapping("/channels/create")
    public String getCreateChannelForm(@RequestParam UUID serverId, Model model) {
        model.addAttribute("serverId", serverId);
        return "channel-create";
    }

    @GetMapping("/channels")
    public String getChannelListPage(@RequestParam UUID serverId, Model model) {
        model.addAttribute("serverId", serverId);
        return "channels";
    }
}
