package com.sprint.discodeit.sprint.domain.dto.channelDto;

import java.util.List;

public record ChannelSummaryResponseDto(String channelName,
                                        List<String> messageContent) {

}
