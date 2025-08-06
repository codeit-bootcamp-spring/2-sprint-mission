package com.sprint.mission.discodeit.domain.channel.event;

import com.sprint.mission.discodeit.domain.channel.dto.ChannelDto;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreatedEvent(ChannelDto channel, List<UUID> participantIds) {

}
