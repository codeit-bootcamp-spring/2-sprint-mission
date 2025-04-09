package com.sprint.mission.discodeit.dto.channel;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CreatePrivateChannelRequest {

    private List<UUID> participantIds;
}
