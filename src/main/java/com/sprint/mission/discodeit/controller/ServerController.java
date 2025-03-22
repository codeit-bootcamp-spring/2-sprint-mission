package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.display.ServerDisplayItem;
import com.sprint.mission.discodeit.dto.display.ServerDisplayList;
import com.sprint.mission.discodeit.dto.request.CreateServerNameDTO;
import com.sprint.mission.discodeit.dto.request.CreateServerRequestDTO;
import com.sprint.mission.discodeit.dto.request.UpdateServerRequestDTO;
import com.sprint.mission.discodeit.dto.result.CreateServerResult;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/servers")
public class ServerController {
    private final ServerService serverService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<CreateServerResult> createServer(@PathVariable UUID userId, @RequestBody CreateServerNameDTO createServerNameDTO) {
        CreateServerRequestDTO createServerRequestDTO = new CreateServerRequestDTO(userId, createServerNameDTO.name());
        Server server = serverService.create(createServerRequestDTO);
        return ResponseEntity.ok(new CreateServerResult(server.getServerId()));
    }

//    @PutMapping("/join/{serverId}")
//    public ResponseEntity<String> join(@PathVariable String serverId, @RequestBody UserIDDTO userIDDTO) {
//
//        User join = serverService.join(serverId, userIDDTO.userId());
//        return ResponseEntity.ok(join.getName() + " has entered the server");
//    }
//
//    @PutMapping("/quit/{serverId}")
//    public ResponseEntity<String> quit(@PathVariable String serverId, @RequestBody UserIDDTO userIDDTO) {
//        User quit = serverService.quit(serverId, userIDDTO.userId());
//        return ResponseEntity.ok(quit.getName() + " has quit the server");
//    }

    @GetMapping
    public ResponseEntity<ServerDisplayList> findAll(@PathVariable UUID userId) {
        List<Server> servers = serverService.findServerAll(userId);
        List<ServerDisplayItem> list = servers.stream().map(s ->
                new ServerDisplayItem(s.getServerId(), s.getName(), s.getCreatedAt(), s.getUpdatedAt())).toList();
        return ResponseEntity.ok(new ServerDisplayList(list));
    }

    @PutMapping("/update/{serverId}")
    public ResponseEntity<UUID> update(@PathVariable UUID serverId, @RequestBody UpdateServerRequestDTO updateServerRequestDTO) {
        UUID update = serverService.update(serverId, updateServerRequestDTO);

        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{serverId}")
    public ResponseEntity<String> delete(@PathVariable UUID serverId) {
        serverService.delete(serverId);

        return ResponseEntity.ok("Delete successful");

    }
}
