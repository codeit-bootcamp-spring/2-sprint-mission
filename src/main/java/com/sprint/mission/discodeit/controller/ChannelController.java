package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping("/create-public")
    public ResponseEntity<SaveChannelDto> createPublic(
            @RequestParam String channelName
    ) {
        SaveChannelDto saveChannelDto = channelService.createPublicChannel(SaveChannelParamDto.createPublic(channelName));
        return ResponseEntity.ok().body(saveChannelDto);
    }

    @GetMapping("/create-private")
    public ResponseEntity<SaveChannelDto> createPrivate(
            @RequestParam String channelName,
            @RequestParam List<UUID> userList
    ) {
        SaveChannelDto saveChannelDto = channelService.createPrivateChannel(SaveChannelParamDto.createPrivate(channelName, userList));
        return ResponseEntity.ok().body(saveChannelDto);
    }

    @GetMapping("/update")
    public ResponseEntity<?> update(@ModelAttribute UpdateChannelParamDto updateChannelParamDto) {
        channelService.updateChannel(updateChannelParamDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/delete")
    public ResponseEntity<?> delete(
            @RequestParam UUID channelUUID
    ) {
        channelService.deleteChannel(channelUUID);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/find-my-channel")
    public ResponseEntity<?> findMyChannel(@RequestBody FindAllByUserIdRequestDto findAllByUserIDRequestDto) {
        List<FindChannelDto> findMyChannelList = channelService.findAllByUserId(findAllByUserIDRequestDto);
        return ResponseEntity.ok().body(findMyChannelList);
    }
}
