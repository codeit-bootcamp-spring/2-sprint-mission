package com.sprint.mission.discodeit.adapter.inbound.server;

import com.sprint.mission.discodeit.adapter.inbound.server.dto.CreateServerNameDTO;
import com.sprint.mission.discodeit.adapter.inbound.server.dto.ServerCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.server.dto.ServerCreateResult;
import com.sprint.mission.discodeit.adapter.inbound.server.dto.ServerDisplayItem;
import com.sprint.mission.discodeit.adapter.inbound.server.dto.ServerDisplayList;
import com.sprint.mission.discodeit.core.server.entity.Server;
import com.sprint.mission.discodeit.core.server.usecase.ServerService;
import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{userId}")
public class ServerController {

  private final ServerService serverService;

  @PostMapping("/create")
  @ResponseBody
  public ResponseEntity<ServerCreateResult> createServer(@PathVariable UUID userId,
      @RequestBody CreateServerNameDTO requestDTO) {

    ServerCreateRequestDTO serverCreateRequestDTO = new ServerCreateRequestDTO(userId,
        requestDTO.name());
    Server server = serverService.create(serverCreateRequestDTO);
    return ResponseEntity.ok(new ServerCreateResult(server.getServerId()));
  }

  @PostMapping("/join/{serverId}")
  public ResponseEntity<String> join(@PathVariable UUID userId, @PathVariable UUID serverId) {

    User join = serverService.join(serverId, userId);
    return ResponseEntity.ok(join.getName() + " has entered the server");
  }

  @PutMapping("/quit/{serverId}")
  public ResponseEntity<String> quit(@PathVariable UUID userId, @PathVariable UUID serverId) {

    User quit = serverService.quit(serverId, userId);
    return ResponseEntity.ok(quit.getName() + " has quit the server");
  }

  @GetMapping
  public ResponseEntity<ServerDisplayList> findAll(@PathVariable UUID userId) {

    List<Server> servers = serverService.findServerAll(userId);

    List<ServerDisplayItem> list = servers.stream().map(s ->
            new ServerDisplayItem(s.getServerId(), s.getName(), s.getCreatedAt(), s.getUpdatedAt()))
        .toList();
    return ResponseEntity.ok(new ServerDisplayList(list));
  }

//  @PutMapping("/update/{serverId}")
//  public ResponseEntity<UUID> update(@PathVariable UUID userId, @PathVariable UUID serverId,
//      @RequestBody UpdateServerRequestDTO updateServerRequestDTO) {
//
//    UUID update = serverService.update(serverId, userId, updateServerRequestDTO);
//
//    return ResponseEntity.ok(update);
//  }

  @DeleteMapping("/delete/{serverId}")
  public ResponseEntity<String> delete(@PathVariable UUID userId, @PathVariable UUID serverId) {

    serverService.delete(serverId, userId);
    return ResponseEntity.ok("Delete successful");

  }
}
