package com.sprint.mission.discodeit.Factory;

import com.sprint.mission.discodeit.composit.Channel;
import com.sprint.mission.discodeit.entity.Server;

public interface Factory<T> {
    T create(String name);
}
