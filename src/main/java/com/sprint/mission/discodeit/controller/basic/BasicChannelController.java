package com.sprint.mission.discodeit.controller.basic;

import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.service.basic.ChannelService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/channels")
@RequiredArgsConstructor
public class BasicChannelController {
    private final ChannelService channelService;
    private static final Logger logger = LoggerFactory.getLogger(BasicChannelController.class);

    @PostMapping("/create/public")
    public ResponseEntity<ChannelDto> createPublic(@RequestBody PublicChannelCreateRequest request){
        ChannelDto response = channelService.createPublic(request);
        logger.info("Successfully create public channel: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create/private")
    public ResponseEntity<ChannelDto> createPrivate(@RequestBody PrivateChannelCreateRequest request){
        ChannelDto response = channelService.createPrivate(request);
        logger.info("Successfully create private channel: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/public")
    public ResponseEntity<ChannelDto> updateChannel(@RequestBody ChannelUpdateRequest request){
        ChannelDto response = channelService.update(request);
        logger.info("Successfully update public channel: {}", request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<String> deleteChannel(@PathVariable String channelId){
        channelService.delete(UUID.fromString(channelId));
        logger.info("Successfully delete channel: {}", channelId);
        return ResponseEntity.ok("Successfully delete channel: " + channelId);
    }

    @GetMapping("/{userId}/findChannels")
    public ResponseEntity<List<ChannelDto>> findChannelsByUserId(@PathVariable String userId){
        List<ChannelDto> response = channelService.findAllByUserId(UUID.fromString(userId));
        logger.info("Successfully find channels by userId: {}", userId);
        return ResponseEntity.ok(response);
    }
}
