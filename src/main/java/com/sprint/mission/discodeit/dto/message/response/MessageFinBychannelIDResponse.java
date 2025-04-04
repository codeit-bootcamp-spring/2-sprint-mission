package com.sprint.mission.discodeit.dto.message.response;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public record MessageFinBychannelIDResponse(
        List<Message> message
) {
}
