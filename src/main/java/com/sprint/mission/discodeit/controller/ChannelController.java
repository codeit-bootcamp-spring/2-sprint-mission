package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
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
@RequestMapping("/api/channel")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    private ResponseEntity<ChannelDto> createPublicChannel(
            @RequestBody PublicChannelCreateRequest publicChannelCreateRequest
    ) {
        Channel createPublicChannel = channelService.create(publicChannelCreateRequest);
        ChannelDto channelDto = channelService.find(createPublicChannel.getId());

        return new ResponseEntity<>(channelDto, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    private ResponseEntity<ChannelDto> createPrivateChannel(
            @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest
    ) {
        Channel createPrivateChannel = channelService.create(privateChannelCreateRequest);
        ChannelDto channelDto = channelService.find(createPrivateChannel.getId());

        return new ResponseEntity<>(channelDto, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    private ResponseEntity<ChannelDto> updatePublicChannel(
            @RequestParam UUID channelId,
            @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {

        Channel updatePublicChannel = channelService.update(channelId, publicChannelUpdateRequest);
        ChannelDto channelDto = channelService.find(updatePublicChannel.getId());

        return new ResponseEntity<>(channelDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    private ResponseEntity<Void> deleteChannel(
            @RequestParam UUID channelId){

        channelService.delete(channelId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    private ResponseEntity<List<ChannelDto>> getChannelByUser(
            @RequestParam UUID userId) {

        List<ChannelDto> channelDtos = channelService.findAllByUserId(userId);

        return new ResponseEntity<>(channelDtos, HttpStatus.OK);
    }
}
