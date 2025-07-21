package com.sprint.mission.discodeit.event;

import java.util.UUID;

public class BinaryContentUploadEvent {
    private final UUID contentId;
    private final byte[] data;

    public BinaryContentUploadEvent(UUID contentId, byte[] data) {
        this.contentId = contentId;
        this.data = data;
    }

    public UUID getContentId() {
        return contentId;
    }

    public byte[] getData() {
        return data;
    }
}
