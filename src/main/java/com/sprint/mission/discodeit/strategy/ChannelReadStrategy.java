package com.sprint.mission.discodeit.strategy;

import com.sprint.mission.discodeit.dto.channel.ChannelReadResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.model.ChannelType;

public interface ChannelReadStrategy {
    ChannelType getSupportChannelType(); // 해당 전략이 특정 채널 타입을 처리할 수 있는지 확인
    ChannelReadResponse toDto(Channel channel);
}
