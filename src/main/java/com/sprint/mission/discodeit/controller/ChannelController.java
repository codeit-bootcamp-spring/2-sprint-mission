package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.IdResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.service.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channel")
public class ChannelController {
    private final ChannelService channelService;

    // 공개 채널 생성
    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ResponseEntity<IdResponse> createPublic(@RequestBody PublicChannelRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.ok(IdResponse.of(channel.getId()));
    }

    // 비공개 채널 생성
    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<IdResponse> createPrivate(@RequestBody PrivateChannelRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.ok(IdResponse.of(channel.getId()));
    }

    // 공개 채널 정보 수정
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<IdResponse> updatePublic(@PathVariable UUID id,
                                                   @RequestBody ChannelUpdateRequest request) {
        Channel channel = channelService.update(id, request);
        return ResponseEntity.ok(IdResponse.of(channel.getId()));
    }

    // 채널 삭제
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        channelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 특정 사용자가 볼 수 있는 모든 채널 목록을 조회
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> findAllByUserId(@PathVariable UUID userId) {
        List<ChannelDto> response = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(response);
    }


}
