package com.sprint.discodeit.sprint5.repository.file;

import com.sprint.discodeit.sprint5.domain.entity.Channel;
import com.sprint.discodeit.sprint5.repository.util.FindRepository;
import com.sprint.discodeit.sprint5.repository.util.SaveRepository;

public interface ChannelRepository extends SaveRepository<Channel>, FindRepository<Channel> {
}
