package com.sprint.mission.discodeit.provider;

import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.updater.MessageUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
public class DefaultMessageUpdaterProvider implements MessageUpdaterProvider {
    private final List<MessageUpdater> messageUpdaters;

    @Override
    public List<MessageUpdater> getApplicableUpdaters(Message message, MessageUpdateRequest messageUpdateRequest) {
        return messageUpdaters.stream()
                .filter(updater -> updater.supports(message, messageUpdateRequest))
                .toList();
    }
}
