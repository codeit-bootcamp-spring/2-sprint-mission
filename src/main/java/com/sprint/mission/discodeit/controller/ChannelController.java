package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelDTO;
import com.sprint.mission.discodeit.dto.CreatePublicChannelDTO;
import com.sprint.mission.discodeit.dto.UpdateChannelDTO;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    // POST 공개채널 생성
    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponseDTO> createPublicChannel(@RequestBody CreatePublicChannelDTO createPublicChannelDTO) {
        ChannelResponseDTO created = channelService.createPublic(createPublicChannelDTO);
        return ResponseEntity.ok(created);
    }

    // POST 비공개채널 생성
    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponseDTO> createPrivateChannel(@RequestBody CreatePrivateChannelDTO createPrivateChannelDTO) {
        ChannelResponseDTO created = channelService.createPrivate(createPrivateChannelDTO);
        return ResponseEntity.ok(created);
    }

    // PUT 공개채널 정보 수정
    @RequestMapping(value = "/public/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updatePublicChannel(@RequestBody UpdateChannelDTO updateChannelDTO){
        channelService.update(updateChannelDTO);
        return ResponseEntity.ok().build();
    }

    // DELETE 채널 삭제
    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePublicChannel(@PathVariable("channelId") String channelId){
        channelService.delete(UUID.fromString(channelId));
        return ResponseEntity.ok().build();
    }

    // GET 특정 사용자가 볼 수 있는 채널 목록 조회
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelResponseDTO>> getAllChannel(
            @PathVariable("userId") String userId
    ){
        List<ChannelResponseDTO> channels = channelService.findAll()
                .stream().filter(channelResponseDTO -> {
                    // 공개 = 볼 수 있음, 비공개 = userId를 포함하고 있으면 볼 수 있음
                    if(channelResponseDTO.getChannelType() == ChannelType.PUBLIC){
                        return true;
                    }
                    else if(channelResponseDTO.getChannelType() == ChannelType.PRIVATE){
                        return channelResponseDTO.getUserIds() != null && channelResponseDTO.getUserIds().contains(UUID.fromString(userId));
                    }
                    return false;
                })
                // 수정 가능한 리스트 반환
                // to.List()는 불변리스트를 반환
                .collect(Collectors.toList());
        return ResponseEntity.ok(channels);
    }
}
