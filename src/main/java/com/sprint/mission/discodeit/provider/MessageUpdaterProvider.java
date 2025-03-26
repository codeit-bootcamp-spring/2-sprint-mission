package com.sprint.mission.discodeit.provider;

import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.updater.MessageUpdater;

import java.util.List;

public interface MessageUpdaterProvider {
    List<MessageUpdater> getApplicableUpdaters(Message message, MessageUpdateRequest messageUpdateRequest);
}
