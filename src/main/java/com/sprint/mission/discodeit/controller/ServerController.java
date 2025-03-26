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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{userId}/servers")
public class ServerController {
    private final ServerService serverService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<CreateServerResult> createServer(@PathVariable UUID userId, @RequestBody CreateServerNameDTO requestDTO) {


        ServerCreateRequestDTO serverCreateRequestDTO = new ServerCreateRequestDTO(userId, requestDTO.name());
        Server server = serverService.create(serverCreateRequestDTO);
        return ResponseEntity.ok(new CreateServerResult(server.getServerId()));
    }

    @PostMapping("/{serverId}/join")
    public ResponseEntity<String> join(@PathVariable UUID userId, @PathVariable UUID serverId) {


        User join = serverService.join(serverId, userId);
        return ResponseEntity.ok(join.getName() + " has entered the server");
    }

    @PutMapping("/{serverId}/quit")
    public ResponseEntity<String> quit(@PathVariable UUID userId, @PathVariable UUID serverId) {


        User quit = serverService.quit(serverId, userId);
        return ResponseEntity.ok(quit.getName() + " has quit the server");
    }

    @GetMapping
    public ResponseEntity<ServerDisplayList> findAll(@PathVariable UUID userId) {


        List<Server> servers = serverService.findServerAll(userId);

        List<ServerDisplayItem> list = servers.stream().map(s ->
                new ServerDisplayItem(s.getServerId(), s.getName(), s.getCreatedAt(), s.getUpdatedAt())).toList();
        return ResponseEntity.ok(new ServerDisplayList(list));
    }

    @PutMapping("/{serverId}/update")
    public ResponseEntity<UUID> update(@PathVariable UUID userId, @PathVariable UUID serverId, @RequestBody UpdateServerRequestDTO updateServerRequestDTO) {

        UUID update = serverService.update(serverId, userId, updateServerRequestDTO);

        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{serverId}/delete")
    public ResponseEntity<String> delete(@PathVariable UUID userId, @PathVariable UUID serverId) {


        serverService.delete(serverId, userId);
        return ResponseEntity.ok("Delete successful");

    }
}
