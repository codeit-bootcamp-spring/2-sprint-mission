package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.DTO.ReadStatus.ReadStatusDto;
import com.sprint.mission.discodeit.DTO.ReadStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    //메세지 수신 정보 생성
    @RequestMapping(method = RequestMethod.POST)
    public ReadStatusDto createReadStatus(@RequestBody CreateReadStatusDto request){
        return readStatusService.create(request);
    }

    //메세지 수신 정보 수정
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ReadStatusDto updateReadStatus(@PathVariable UUID id, @RequestBody UpdateReadStatusDto request){
        request = new UpdateReadStatusDto(id, request.lastReadAt());
        return readStatusService.update(request);
    }

    //특정 사용자의 메세지 수신 정보 조회
    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}")
    public List<ReadStatusDto> findReadStatusByUserId(@PathVariable("userId") UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }
}
