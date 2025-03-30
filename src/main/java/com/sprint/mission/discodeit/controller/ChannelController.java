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
    public ResponseEntity<ApiResponse<SaveChannelDto>> createPublic(
            @RequestParam String channelName
    ) {
        SaveChannelDto saveChannelDto = channelService.createPublicChannel(SaveChannelParamDto.createPublic(channelName));
        return ResponseEntity.ok(ApiResponse.success(saveChannelDto));
    }

    @GetMapping("/create-private")
    public ResponseEntity<ApiResponse<SaveChannelDto>> createPrivate(
            @RequestParam String channelName,
            @RequestParam List<UUID> userList
    ) {
        SaveChannelDto saveChannelDto = channelService.createPrivateChannel(SaveChannelParamDto.createPrivate(channelName, userList));
        return ResponseEntity.ok(ApiResponse.success(saveChannelDto));
    }

    @GetMapping("/update")
    public ResponseEntity<ApiResponse<Void>> update(@ModelAttribute UpdateChannelParamDto updateChannelParamDto) {
        channelService.updateChannel(updateChannelParamDto);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestParam UUID channelUUID
    ) {
        channelService.deleteChannel(channelUUID);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/find-my-channel")
    public ResponseEntity<ApiResponse<List<FindChannelDto>>> findMyChannel(@RequestBody FindAllByUserIdRequestDto findAllByUserIDRequestDto) {
        List<FindChannelDto> findMyChannelList = channelService.findAllByUserId(findAllByUserIDRequestDto);
        return ResponseEntity.ok(ApiResponse.success(findMyChannelList));
    }
}
