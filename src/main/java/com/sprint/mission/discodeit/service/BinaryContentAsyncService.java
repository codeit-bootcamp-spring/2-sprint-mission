package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface BinaryContentAsyncService {
    void uploadFile(UUID binaryContentId, byte[] bytes);
}