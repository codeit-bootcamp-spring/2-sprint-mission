package com.sprint.discodeit.repository;

import com.sprint.discodeit.domain.ChannelType;
import com.sprint.discodeit.domain.entity.Channel;
import com.sprint.discodeit.repository.util.FindRepository;
import com.sprint.discodeit.repository.util.SaveRepository;
import java.util.List;

public interface ChannelRepository extends SaveRepository<Channel>, FindRepository<Channel> {
}
