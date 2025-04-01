package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelDeleteRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.util.LogMapUtil;
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
    public ResponseEntity<Channel> createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        Channel publicChannel = channelService.create(request);
        log.info("{}", LogMapUtil.of("action", "createPublic")
                .add("publicChannel", publicChannel));

        return ResponseEntity.ok(publicChannel);
    }

    @RequestMapping(value = "/createPrivate", method = RequestMethod.POST)
    public ResponseEntity<Channel> createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        Channel privateChannel = channelService.create(request);
        log.info("{}", LogMapUtil.of("action", "createPrivate")
                .add("privateChannel", privateChannel));

        return ResponseEntity.ok(privateChannel);
    }

    @RequestMapping(value = "/updatePublic", method = RequestMethod.PUT)
    public ResponseEntity<Channel> updatePublicChannel(@RequestBody PublicChannelUpdateRequest request) {
        Channel updated = channelService.update(request);
        log.info("{}", LogMapUtil.of("action", "updatePublic")
                .add("updated", updated));

        return ResponseEntity.ok(updated);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Channel> deleteChannel(@RequestBody ChannelDeleteRequest request) {
        channelService.delete(request);
        log.info("{}", LogMapUtil.of("action", "delete")
                .add("request", request));

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/listAccessibleChannels", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> listAccessibleChannels(@RequestParam UUID userKey) {
        List<ChannelDto> listAccessibleChannels = channelService.readAllByUserKey(userKey);
        log.info("{}", LogMapUtil.of("action", "listAccessibleChannels")
                .add("listAccessibleChannels", listAccessibleChannels));

        return ResponseEntity.ok(listAccessibleChannels);
    }
}
