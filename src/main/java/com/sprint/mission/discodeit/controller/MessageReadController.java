package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDTO;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/readStatus")
@RequiredArgsConstructor
public class MessageReadController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ReadStatus createReadStatus(@RequestBody CreateReadStatusDTO dto) {
        return readStatusService.create(dto);
    }

    @RequestMapping(value = "/user/{userId}/channel/{channelId}", method = RequestMethod.PUT)
    public ReadStatus updateReadStatus(@PathVariable("userId") UUID userId,
                                       @PathVariable("channelId") UUID channelId,
                                       @RequestBody UpdateReadStatusDTO dto) {
        return readStatusService.updateByChannelIdAndUserId(userId, channelId, dto);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<ReadStatus> getReadStatus(@PathVariable("userId") UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }
}
