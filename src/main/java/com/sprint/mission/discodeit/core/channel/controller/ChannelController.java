package com.sprint.mission.discodeit.core.channel.controller;

import com.sprint.mission.discodeit.core.channel.controller.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.core.channel.controller.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.controller.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.controller.response.ChannelDeleteResponse;
import com.sprint.mission.discodeit.core.channel.controller.response.ChannelResponse;
import com.sprint.mission.discodeit.core.channel.usecase.ChannelService;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Channel", description = "채널 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  /**
   * <h2>공개 채널 생성 메서드</h2>
   * 공개채널을 생성한다.
   *
   * @param requestBody 채널명, 채널 설명
   * @return 채널 아이디, 채널 타입 ,채널명, 채널 설명, 참가 유저들, 최근 메시지 시각
   */
  @PostMapping("/public")
  public ResponseEntity<ChannelResponse> create(
      @RequestBody @Valid PublicChannelCreateRequest requestBody) {
    //Dto Mapper를 통해서 request를 command로 교체
    //why? 이상한 요청이 들어와서 서비스가 뻗는 것을 막기 위함 = 영향을 적게 하기 위함
    CreatePublicChannelCommand command = ChannelDtoMapper.toCreatePublicChannelCommand(
        requestBody);
    //채널 생성하기, 반환 DTO를 받음
    ChannelResult result = channelService.create(command);
    //Dto Mapper를 통해서 result를 응답 dto로 변환
    ChannelResponse response = ChannelDtoMapper.toCreateResponse(result);
    return ResponseEntity.ok(response);
  }

  /**
   * <h2>비공개 채널 생성 메서드</h2>
   * 비공개 채널을 생성한다.
   *
   * @param requestBody 사용자 ID 목록
   * @return 채널 아이디, 채널 타입 ,채널명, 채널 설명, 참가 유저들, 최근 메시지 시각
   */
  @PostMapping("/private")
  public ResponseEntity<ChannelResponse> create(
      @RequestBody @Valid PrivateChannelCreateRequest requestBody) {
    //Dto Mapper를 통해서 request를 command로 교체
    CreatePrivateChannelCommand command = ChannelDtoMapper.toCreatePrivateChannelCommand(
        requestBody);
    //채널 생성하기, 반환 DTO를 받음
    ChannelResult result = channelService.create(command);

    return ResponseEntity.ok(ChannelDtoMapper.toCreateResponse(result));
  }

  /**
   * <h2>채널 목록 찾기 메서드</h2>
   * DB에 저장된 채널 목록을 찾는다.
   *
   * @param userId
   * @return 채널 아이디, 채널 타입 ,채널명, 채널 설명, 참가 유저들, 최근 메시지 시각
   */
  @GetMapping
  public ResponseEntity<List<ChannelResult>> findAll(@RequestParam("userId") UUID userId) {
    List<ChannelResult> result = channelService.findAllByUserId(userId);

    return ResponseEntity.ok(result);
  }

  /**
   * <h2> 채널 업데이트 메서드</h2>
   * 채널을 업데이트한다.
   *
   * @param channelId   업데이트할 대상
   * @param requestBody 새 채널명, 새 채널 설명
   * @return 채널 아이디, 채널 타입 ,채널명, 채널 설명, 참가 유저들, 최근 메시지 시각
   */
  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelResponse> update(@PathVariable UUID channelId,
      @RequestBody @Valid ChannelUpdateRequest requestBody) {
    //Dto Mapper를 통해서 request를 command로 교체
    UpdateChannelCommand command = ChannelDtoMapper.toUpdateChannelCommand(channelId, requestBody);
    ChannelResult result = channelService.update(command);
    return ResponseEntity.ok(ChannelDtoMapper.toCreateResponse(result));
  }

  /**
   * <h2>채널 삭제 메서드</h2>
   * 채널을 삭제한다.
   *
   * @param channelId 삭제할 채널 아이디
   * @return 성공 시 true를 반환함
   */
  @DeleteMapping("/{channelId}")
  public ResponseEntity<ChannelDeleteResponse> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.ok(new ChannelDeleteResponse(true));
  }

}
