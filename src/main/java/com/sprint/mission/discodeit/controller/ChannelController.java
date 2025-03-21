package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.UserFindDTO;
import com.sprint.mission.discodeit.dto.request.CreateChannelRequestDTO;
import com.sprint.mission.discodeit.dto.request.JoinQuitChannelRequestDTO;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/create")
    public ResponseEntity<UUID> create(@RequestBody CreateChannelRequestDTO channelCreateDTO) {
        UUID id = channelService.create(channelCreateDTO);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/join")
    public ResponseEntity<UserFindDTO> join(@RequestBody JoinQuitChannelRequestDTO joinQuitChannelRequestDTO) {
        UserFindDTO join = channelService.join(joinQuitChannelRequestDTO);
        return ResponseEntity.ok(join);
    }

    @PutMapping("/quit")
    public ResponseEntity<UserFindDTO> quit(@RequestBody JoinQuitChannelRequestDTO joinQuitChannelRequestDTO) {
        UserFindDTO quit = channelService.quit(joinQuitChannelRequestDTO);

        return ResponseEntity.ok(quit);
    }

    @GetMapping("/{serverId}")
    public ResponseEntity<List<ChannelFindDTO>> findAll(@PathVariable String serverId) {
        List<ChannelFindDTO> list = channelService.findAllByServerAndUser(serverId);
        return ResponseEntity.ok(list);
    }

//    @PutMapping("/update/{userId}")
//    public ResponseEntity<User> update(@PathVariable String , @RequestBody UserUpdateRequestDTO ) {
//
//
//    }

    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<String> delete(@PathVariable String channelId) {
        boolean isDelete = channelService.delete(channelId);
        if (isDelete == true) {
            return ResponseEntity.ok("Delete successful");
        } else {
            return ResponseEntity.status(401).body("Delete failed");
        }
    }
}
