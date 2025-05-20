package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.channel.Channel;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ChannelMapper {

  ChannelResponse toResponse(
      Channel channel,
      List<UserResponse> participants,
      Instant lastMessageAt
  );
}
