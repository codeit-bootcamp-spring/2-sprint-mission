package com.sprint.mission.discodeit.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AsyncFailedEvent {
    private final String taskName;
    private final Throwable exception;
}
