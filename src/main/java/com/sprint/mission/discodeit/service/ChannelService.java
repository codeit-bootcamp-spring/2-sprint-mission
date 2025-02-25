package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

public interface ChannelService {
    public abstract Message write(String str);
}
