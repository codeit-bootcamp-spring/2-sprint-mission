package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.display.ChannelDisplayList;
import com.sprint.mission.discodeit.dto.request.CreateChannelRequestDTO;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/create")
    public ResponseEntity<UUID> create(@RequestBody CreateChannelRequestDTO channelCreateDTO) {
        UUID id = channelService.create(channelCreateDTO);
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public ResponseEntity<ChannelDisplayList> findAll(@PathVariable UUID serverId) {
        List<ChannelFindDTO> list = channelService.findAllByServerAndUser(serverId);
        return ResponseEntity.ok(new ChannelDisplayList(list));
    }
//    @PutMapping("/join")
//    public ResponseEntity<UserFindDTO> join(@RequestBody JoinQuitChannelRequestDTO joinQuitChannelRequestDTO) {
//        UserFindDTO join = channelService.join(joinQuitChannelRequestDTO);
//        return ResponseEntity.ok(join);
//    }
//
//    @PutMapping("/quit")
//    public ResponseEntity<UserFindDTO> quit(@RequestBody JoinQuitChannelRequestDTO joinQuitChannelRequestDTO) {
//        UserFindDTO quit = channelService.quit(joinQuitChannelRequestDTO);
//
//        return ResponseEntity.ok(quit);

//    }

//    @PutMapping("/update/{userId}")
//    public ResponseEntity<User> update(@PathVariable String , @RequestBody UserUpdateRequestDTO ) {
//
//
//    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<String> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.ok("Delete successful");

    }
}
