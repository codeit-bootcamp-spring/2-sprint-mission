package com.sprint.mission.discodeit.event;

import lombok.Getter;

import java.util.*;

@Getter
public class BinaryContentsDeleteEvent {
    private final List<UUID> attachmentIds;

    public BinaryContentsDeleteEvent(List<UUID> attachmentIds) {
        this.attachmentIds = attachmentIds;
    }
}