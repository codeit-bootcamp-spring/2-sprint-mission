package com.sprint.discodeit.sprint.config;

import com.sprint.discodeit.sprint.domain.entity.Message;
import java.util.List;

public interface UserPrivateChannelMessageFindRepository {

    List<Message> findMessagesByChannelGroupedByUser(Long userId, List<Long> channelIds);
}
