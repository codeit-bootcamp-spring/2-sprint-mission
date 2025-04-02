package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BaseResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/channels/public")
    public ResponseEntity<BaseResponseDto> createUser(@RequestBody ChannelCreatePublicDto channelCreatePublicDto) {
        Channel channel = channelService.createPublic(channelCreatePublicDto);
        return ResponseEntity.ok(BaseResponseDto.success(channel.getId() + " 채널 생성이 완료되었습니다."));
    }

    @PostMapping("/channels/private")
    public ResponseEntity<BaseResponseDto> createUser(@RequestBody ChannelCreatePrivateDto channelCreatePrivateDto) {
        Channel channel = channelService.createPrivate(channelCreatePrivateDto);
        return ResponseEntity.ok(BaseResponseDto.success(channel.getId() + " 채널 생성이 완료되었습니다."));
    }

    @PutMapping("/channels/{id}")
    public ResponseEntity<BaseResponseDto> updateChannel(@PathVariable("id") UUID channelId,
                                                         @RequestBody ChannelUpdateDto channelUpdateDto) {
        Channel channel = channelService.update(channelId, channelUpdateDto);
        return ResponseEntity.ok(BaseResponseDto.success(channel.getId() + " 채널 변경이 완료되었습니다."));
    }

    @DeleteMapping("/channels/{id}")
    public ResponseEntity<BaseResponseDto> deleteChannel(@PathVariable("id") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.ok(BaseResponseDto.success(channelId + " 채널 삭제가 완료되었습니다."));
    }

    @GetMapping("/users/{userId}/channels")
    public ResponseEntity<List<ChannelDto>> getChannelsByUserId(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }
}
