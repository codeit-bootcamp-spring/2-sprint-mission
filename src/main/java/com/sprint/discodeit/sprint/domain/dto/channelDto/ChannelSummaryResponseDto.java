package com.sprint.discodeit.sprint.domain.dto.channelDto;

import com.sprint.discodeit.sprint.domain.ChannelType;
import com.sprint.discodeit.sprint.domain.entity.PrivateChannel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelSummaryResponseDto(Long  channelId,
                                        String channelName,
                                        String channelDescription) {

}
