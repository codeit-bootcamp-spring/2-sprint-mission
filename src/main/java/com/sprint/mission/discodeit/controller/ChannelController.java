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
    public ResponseEntity<SaveChannelDto> createPublic(@RequestBody SaveChannelParamDto saveChannelParamDto) {
        SaveChannelDto saveChannelDto = channelService.createPublicChannel(saveChannelParamDto);
        return ResponseEntity.ok().body(saveChannelDto);
    }

    @GetMapping("/create-private")
    public ResponseEntity<SaveChannelDto> createPrivate(@RequestBody SaveChannelParamDto saveChannelParamDto) {
        SaveChannelDto saveChannelDto = channelService.createPrivateChannel(saveChannelParamDto);
        return ResponseEntity.ok().body(saveChannelDto);
    }

    @GetMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateChannelParamDto updateChannelParamDto) {
        channelService.updateChannel(updateChannelParamDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/delete{id}")
    public ResponseEntity<?> delete(@RequestParam UUID channelUUID) {
        channelService.deleteChannel(channelUUID);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/find-my-channel")
    public ResponseEntity<?> findMyChannel(@RequestBody FindAllByUserIdRequestDto findAllByUserIDRequestDto) {
        List<FindChannelDto> findMyChannelList = channelService.findAllByUserId(findAllByUserIDRequestDto);
        return ResponseEntity.ok().body(findMyChannelList);
    }
}
