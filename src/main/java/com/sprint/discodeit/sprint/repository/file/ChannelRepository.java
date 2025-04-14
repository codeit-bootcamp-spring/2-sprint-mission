package com.sprint.discodeit.sprint.repository.file;

import com.sprint.discodeit.sprint.domain.entity.Channel;
import com.sprint.discodeit.sprint.repository.util.FindRepository;
import com.sprint.discodeit.sprint.repository.util.SaveRepository;

public interface ChannelRepository extends SaveRepository<Channel>, FindRepository<Channel> {
}
