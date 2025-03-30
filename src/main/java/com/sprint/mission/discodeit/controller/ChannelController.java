package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelDeleteRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;
    private static final Logger log = LoggerFactory.getLogger(ChannelController.class);

    @RequestMapping(value = "/createPublic", method = RequestMethod.POST)
    public ResponseEntity<?> createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        log.info("public 채널 요청 {}", request);
        Channel publicChannel = channelService.create(request);
        log.info("public 채널 정보 {}", publicChannel);

        return ResponseEntity.ok(publicChannel);
    }

    @RequestMapping(value = "/createPrivate", method = RequestMethod.POST)
    public ResponseEntity<?> createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        log.info("private 채널 요청 {}", request);
        Channel privateChannel = channelService.create(request);
        log.info("private 채널 정보 {}", privateChannel);

        return ResponseEntity.ok(privateChannel);
    }

    @RequestMapping(value = "/updatePublic", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePublicChannel(@RequestBody PublicChannelUpdateRequest request) {
        log.info("public 채널 업데이트 요청 {}", request);
        Channel updated = channelService.update(request);
        log.info("업데이트 된 채널 정보 {}", updated);

        return ResponseEntity.ok(updated);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteChannel(@RequestBody ChannelDeleteRequest request) {
        log.info("삭제할 채널 {}", request);
        channelService.delete(request);
        log.info("삭제된 채널 {}", request);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/listAccessibleChannels", method = RequestMethod.GET)
    public ResponseEntity<?> listAccessibleChannels(@RequestParam UUID userKey) {
        List<ChannelDto> listAccessibleChannels = channelService.readAllByUserKey(userKey);
        log.info("{} 가 볼 수 있는 채널 정보 {}", userKey, listAccessibleChannels);

        return ResponseEntity.ok(listAccessibleChannels);
    }
}
