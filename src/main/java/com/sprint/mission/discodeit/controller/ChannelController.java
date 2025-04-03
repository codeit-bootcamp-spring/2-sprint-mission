package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.channel.*;
import com.sprint.mission.discodeit.dto.service.channel.ChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.FindChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.PrivateChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelDTO;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;
    private final ChannelMapper channelMapper;

    @PostMapping("/public")
    public ResponseEntity<CreatePublicChannelResponseDTO> createPublicChannel(@RequestBody @Valid CreatePublicChannelRequestDTO createChannelRequestDTO) {
        ChannelDTO channelDTO =
                channelService.createPublicChannel(channelMapper.toChannelParam(createChannelRequestDTO));
        return ResponseEntity.ok(channelMapper.toChannelResponseDTO(channelDTO));
    }

    @PostMapping("/private")
    public ResponseEntity<CreatePrivateChannelResponseDTO> createPrivateChannel(@RequestBody @Valid CreatePrivateChannelRequestDTO createPrivateChannelRequestDTO) {
        PrivateChannelDTO privateChannelDTO =
                channelService.createPrivateChannel(channelMapper.toPrivateChannelParam(createPrivateChannelRequestDTO));
        return ResponseEntity.ok(channelMapper.toPrivateChannelResponseDTO(privateChannelDTO));
    }

    @PutMapping("/{channelId}")
    public ResponseEntity<UpdateChannelResponseDTO> updateChannel(@PathVariable("channelId") UUID channelId, @RequestBody @Valid UpdateChannelRequestDTO updateChannelRequestDTO) {
        UpdateChannelDTO updateChannelDTO =
                channelService.update(channelId, channelMapper.toUpdateChannelParam(updateChannelRequestDTO));
        return ResponseEntity.ok(channelMapper.toUpdateChannelResponseDTO(updateChannelDTO));
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<DeleteChannelResponseDTO> deleteChannel(@PathVariable("channelId") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.ok(new DeleteChannelResponseDTO(channelId, channelId + "번 채널이 삭제되었습니다."));
    }

    @GetMapping("/{userId}")
    // 현재는 로그인을 유지하는 기능이나 토큰이 없으므로, 임시로 userId를 PathVariable 받아서 검색
    public ResponseEntity<ChannelListResponseDTO> getChannelsByUserId(@PathVariable("userId") UUID userId) {
        List<FindChannelDTO> findChannelDTO = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(new ChannelListResponseDTO(findChannelDTO));
    }

}
