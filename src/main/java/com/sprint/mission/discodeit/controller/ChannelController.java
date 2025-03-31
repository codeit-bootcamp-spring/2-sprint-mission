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
@RequestMapping("/api/channels/")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/create/public", method = RequestMethod.POST)
    public ResponseEntity<ChannelDto> publicChannelCreate(@RequestBody PublicChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        ChannelDto dto = channelService.find(channel.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @RequestMapping(value = "/create/private", method = RequestMethod.POST)
    public ResponseEntity<ChannelDto> privateChannelCreate(@RequestBody PrivateChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        ChannelDto dto = channelService.find(channel.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ChannelDto> delete(@PathVariable UUID id) {
        channelService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ChannelDto> update(@PathVariable UUID id, @RequestBody PublicChannelUpdateRequest request) {
        Channel channel = channelService.update(id, request);
        ChannelDto channelDto = channelService.find(channel.getId());
        return ResponseEntity.status(HttpStatus.OK).body(channelDto);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<ChannelDto> get(@PathVariable UUID id) {
        ChannelDto channelDto = channelService.find(id);
        return ResponseEntity.status(HttpStatus.OK).body(channelDto);
    }

    @RequestMapping(value = "/getAll/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> getAll(@PathVariable UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }
}
