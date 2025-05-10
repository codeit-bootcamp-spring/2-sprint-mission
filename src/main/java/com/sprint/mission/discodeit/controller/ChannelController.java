package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

    private final ChannelService channelService;

    @PostMapping(path = "public")
    public ResponseEntity<ChannelDto> create(@RequestBody PublicChannelCreateRequest request) {
        // log
        log.info("Public channel 생성 요청");
        ChannelDto createdChannel = channelService.create(request);
        log.info("Public channel 생성 완료, channel 이름: {}", createdChannel.name());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdChannel);
    }

    @PostMapping(path = "private")
    public ResponseEntity<ChannelDto> create(@RequestBody PrivateChannelCreateRequest request) {
        log.info("Private channel 생성 요청");
        ChannelDto createdChannel = channelService.create(request);
        log.info("Private channel 생성 완료, channel 이름: {}", createdChannel.name());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdChannel);
    }

    @PatchMapping(path = "{channelId}")
    public ResponseEntity<ChannelDto> update(@PathVariable("channelId") UUID channelId,
        @RequestBody PublicChannelUpdateRequest request) {
        log.debug("Channel 업데이트 요청, channel 이름: {}", request.newName());
        ChannelDto updatedChannel = channelService.update(channelId, request);
        log.info("Channel 업데이트 완료, channel 이름: {}", updatedChannel.name());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedChannel);
    }

    @DeleteMapping(path = "{channelId}")
    public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
        log.warn("Channel 삭제 요청");
        channelService.delete(channelId);
        log.info("Channel 삭제 완료");
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
        log.info("사용자가 소속되어있는 Channel 조회 요청");
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        log.info("조회 완료, 사용자가 소속되어있는 channel 수: {}", channels.size());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(channels);
    }
}
