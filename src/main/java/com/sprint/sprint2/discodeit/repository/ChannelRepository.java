package com.sprint.sprint2.discodeit.repository;

import com.sprint.sprint2.discodeit.entity.Channel;
import com.sprint.sprint2.discodeit.repository.util.FindRepository;
import com.sprint.sprint2.discodeit.repository.util.SaveRepository;

public interface ChannelRepository extends SaveRepository<Channel>, FindRepository<Channel> {
}
