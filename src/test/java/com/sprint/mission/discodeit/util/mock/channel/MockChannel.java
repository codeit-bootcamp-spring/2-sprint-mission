package com.sprint.mission.discodeit.util.mock.channel;

import static com.sprint.mission.discodeit.util.mock.channel.ChannelInfo.CHANNEL_NAME;

import com.sprint.mission.discodeit.application.dto.channel.PrivateChannelCreationRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import java.util.List;
import java.util.UUID;

public class MockChannel {

  private PrivateChannelCreationRequest createMpckPrivateChannel(UserResult loginUser,
      List<UUID> memberIds) {
    return new PrivateChannelCreationRequest(CHANNEL_NAME, loginUser.id(), memberIds);
  }
}
