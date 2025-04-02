package com.sprint.mission.discodeit.core.channel.usecase.dto;

import com.sprint.mission.discodeit.adapter.inbound.channel.dto.ChannelResult;
import java.util.List;

public record ChannelListResult(
    List<ChannelResult> channels
) {

}
