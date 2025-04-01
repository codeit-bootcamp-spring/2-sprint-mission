package com.sprint.mission.discodeit.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

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
