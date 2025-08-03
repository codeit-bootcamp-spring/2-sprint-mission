package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageCreatedEvent {
    private final Message message;
} 