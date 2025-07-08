package com.sprint.mission.discodeit.core.channel.dto;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import com.sprint.mission.discodeit.core.read.repository.JpaReadStatusRepository;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Channel Item")
public record ChannelDto(
    @Schema(description = "Channel Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,
    @Schema(description = "Channel Type", example = "PUBLIC")
    ChannelType type,
    @Schema(description = "Channel username", example = "string")
    String name,
    @Schema(description = "Channel newDescription", example = "string")
    String description,
    @Schema(
        description = "사용자 ID 목록",
        example = "[\"3fa85f64-5717-4562-b3fc-2c963f66afa6\"]"
    )
    List<UserDto> participants,
    @Schema(description = "최근 메시지 시각", example = "2025-04-03T01:49:44.983Z")
    Instant lastMessageAt
) {

  public static ChannelDto create(Channel channel, JpaMessageRepository messageRepository,
      JpaReadStatusRepository readStatusRepository) {
    Instant lastMessageAt = messageRepository.findLastMessageAtByChannelId(channel.getId())
        .orElse(Instant.MIN);

    List<UserDto> userIdList = new ArrayList<>();
    readStatusRepository.findAllByChannel_Id(channel.getId())
        .stream().map(readStatus -> {
          User user = readStatus.getUser();
          return UserDto.from(user);
        })
        .forEach(userIdList::add);

    return ChannelDto.builder()
        .id(channel.getId())
        .type(channel.getType())
        .name(channel.getName())
        .description(channel.getDescription())
        .participants(userIdList)
        .lastMessageAt(lastMessageAt)
        .build();
  }

}
