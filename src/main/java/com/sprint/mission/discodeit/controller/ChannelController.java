package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.Channel.ChannelDetailsDto;
import com.sprint.mission.discodeit.DTO.Channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.DTO.Channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.DTO.Channel.UpdateChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    //공개 채널 생성
    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ChannelDetailsDto createPublicChannel(@RequestBody CreatePublicChannelDto request) {
        return channelService.createPublicChannel(request);
    }

    //비공개 채널 생성
    @RequestMapping(method = RequestMethod.POST, value = "/private")
    public ChannelDetailsDto createPrivateChannel(@RequestBody CreatePrivateChannelDto request) {
        return channelService.createPrivateChannel(request);
    }

    //공개 채널 정보 수정
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ChannelDetailsDto updatePubicChannel(@PathVariable UUID id, @RequestBody UpdateChannelDto request) {
        channelService.update(id, request.name(), request.description());
        return channelService.find(id);
    }

    //채널 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteChannel(@PathVariable UUID id) {
        channelService.delete(id);
    }

    //특정 사용자가 볼 수 있는 모든 채널 목록 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<ChannelDetailsDto> findAllChannelsByUserId(@RequestParam UUID userId) {
        return channelService.findAllByUserId(userId);
    }

}

