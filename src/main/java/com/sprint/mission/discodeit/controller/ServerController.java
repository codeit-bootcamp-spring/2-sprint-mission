package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/servers")
public class ServerController {
    private final ServerService serverService;

//    @PostMapping("/create")
//    public ResponseEntity<Server> create(@RequestBody ServerCreateRequestDTO serverCreateRequestDTO) {
//        Server server = serverService.create(serverCreateRequestDTO);
//        return ResponseEntity.ok(server);
//    }
//
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
//
//    @GetMapping("/{ownerId}")
//    public ResponseEntity<List<Server>> findAll(@PathVariable String ownerId) {
//        List<Server> servers = serverService.findServerAll(ownerId);
//        return ResponseEntity.ok(servers);
//    }

//    @PutMapping("/update/{userId}")
//    public ResponseEntity<User> update(@PathVariable String , @RequestBody UserUpdateRequestDTO ) {
//
//
//    }

    @DeleteMapping("/delete/{serverId}")
    public ResponseEntity<String> delete(@PathVariable String serverId) {
        boolean isDelete = serverService.delete(serverId);
        if (isDelete == true) {
            return ResponseEntity.ok("Delete successful");
        } else {
            return ResponseEntity.status(401).body("Delete failed");
        }
    }
}
