package com.sprint.mission.discodeit.Factory;

import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.jcf.JCFServerService;

public interface Factory {
    JCFServerService create(String str);
}
