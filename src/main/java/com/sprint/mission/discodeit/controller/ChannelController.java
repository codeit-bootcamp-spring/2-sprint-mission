package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ResponseEntity<Channel> createPublic(@RequestBody PublicChannelCreateRequest request){
        Channel craeted = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(craeted);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<Channel> createPrivate(@RequestBody PrivateChannelCreateRequest request) {
        List<UUID> participants = request.participantIds() == null
                ? List.of()
                : request.participantIds();
        
        PrivateChannelCreateRequest safeRequest = new PrivateChannelCreateRequest(participants);

        Channel channel = channelService.create(safeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }


    @RequestMapping(value = "/public/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<Channel> updatePublic(
            @PathVariable UUID channelId,
            @RequestBody PublicChannelUpdateRequest request) {
        Channel updated = channelService.update(channelId, request);
        return ResponseEntity.ok(updated);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID channelId){
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> findAllByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }

}
