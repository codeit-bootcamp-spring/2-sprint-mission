package com.sprint.mission.discodeit.adapter.inbound.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServerPageController {
    @GetMapping("/server-index")
    public String getServerIndexPage() {
        return "server-index";
    }

    @GetMapping("/server-create")
    public String getCreateServerForm() {
        return "server-create";
    }

    @GetMapping("/servers")
    public String getServersPage() {
        return "servers";
    }
}
