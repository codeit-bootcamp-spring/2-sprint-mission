package com.sprint.mission.discodeit.application.dto.channel;

import java.util.UUID;

public record PrivateChannelAddMemeberRequest(UUID channelId, String friendEmail) {
}
