package com.sprint.mission.discodeit.dto.channel;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class CreatePrivateChannelRequest {

  private List<UUID> participantIds;
}
