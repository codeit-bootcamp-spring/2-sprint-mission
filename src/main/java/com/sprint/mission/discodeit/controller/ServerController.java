package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.create.CreateServerNameDTO;
import com.sprint.mission.discodeit.dto.create.ServerCreateRequestDTO;
import com.sprint.mission.discodeit.dto.display.ServerDisplayItem;
import com.sprint.mission.discodeit.dto.display.ServerDisplayList;
import com.sprint.mission.discodeit.dto.result.CreateServerResult;
import com.sprint.mission.discodeit.dto.update.UpdateServerRequestDTO;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ServerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/servers")
public class ServerController {
    private final ServerService serverService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<CreateServerResult> createServer(@RequestBody CreateServerNameDTO requestDTO, HttpServletRequest httpRequest) {
        UUID userId = (UUID) httpRequest.getAttribute("userId");
        ServerCreateRequestDTO serverCreateRequestDTO = new ServerCreateRequestDTO(userId, requestDTO.name());
        Server server = serverService.create(serverCreateRequestDTO);
        return ResponseEntity.ok(new CreateServerResult(server.getServerId()));
    }

    @PostMapping("/{serverId}/join")
    public ResponseEntity<String> join(@PathVariable UUID serverId,HttpServletRequest httpRequest ) {
        UUID userId = (UUID) httpRequest.getAttribute("userId");
        User join = serverService.join(serverId, userId);
        return ResponseEntity.ok(join.getName() + " has entered the server");
    }

    @PutMapping("/{serverId}/quit")
    public ResponseEntity<String> quit(@PathVariable UUID serverId,HttpServletRequest httpRequest) {
        UUID userId = (UUID) httpRequest.getAttribute("userId");
        User quit = serverService.quit(serverId, userId);
        return ResponseEntity.ok(quit.getName() + " has quit the server");
    }

    @GetMapping
    public ResponseEntity<ServerDisplayList> findAll(HttpServletRequest httpRequest) {
        UUID userId = (UUID) httpRequest.getAttribute("userId");
        System.out.println("TokenStore 조회 요청 userId: " + userId);

        List<Server> servers = serverService.findServerAll(userId);

        List<ServerDisplayItem> list = servers.stream().map(s ->
                new ServerDisplayItem(s.getServerId(), s.getName(), s.getCreatedAt(), s.getUpdatedAt())).toList();
        return ResponseEntity.ok(new ServerDisplayList(list));
    }

    @PutMapping("/{serverId}/update")
    public ResponseEntity<UUID> update(@PathVariable UUID serverId, @RequestBody UpdateServerRequestDTO updateServerRequestDTO, HttpServletRequest httpRequest) {
        UUID userId = (UUID) httpRequest.getAttribute("userId");
        UUID update = serverService.update(serverId, userId, updateServerRequestDTO);

        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{serverId}/delete")
    public ResponseEntity<String> delete(@PathVariable UUID serverId, HttpServletRequest httpRequest) {
        UUID userId = (UUID) httpRequest.getAttribute("userId");

        serverService.delete(serverId, userId);
        return ResponseEntity.ok("Delete successful");

    }
}
