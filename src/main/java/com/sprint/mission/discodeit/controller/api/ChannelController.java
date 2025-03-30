package com.sprint.mission.discodeit.controller.api;


import com.sprint.mission.discodeit.dto.channelService.ChannelCreateByPrivateRequest;
import com.sprint.mission.discodeit.dto.channelService.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelService.ChannelDto;
import com.sprint.mission.discodeit.dto.channelService.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    //DTO 쓰는게 좋은데 고쳐야 함

    @RequestMapping(value = "/public",method = RequestMethod.POST)
    public ResponseEntity<Channel> createByPublic(@RequestBody ChannelCreateRequest request) {
        Channel channel = channelService.create(request);

        return ResponseEntity.ok(channel);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<Channel> createByPrivate(@RequestBody ChannelCreateByPrivateRequest request) {
        Channel channel = channelService.createByPrivate(request);

        return ResponseEntity.ok(channel);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<Channel> update(@PathVariable UUID channelId, @RequestBody ChannelUpdateRequest request) {
        Channel channel = channelService.update(channelId, request);

        return ResponseEntity.ok(channel);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/channels/{Userid}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> findAllByUserId(@PathVariable UUID Userid) {
        List<ChannelDto> channels = channelService.findAllByUserId(Userid);

        return ResponseEntity.ok(channels);
    }
}
