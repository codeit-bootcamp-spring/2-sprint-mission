package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentResult;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {
    UUID put(UUID binaryContentId, byte[] bytes);

    InputStream get(UUID binaryContentId);

    ResponseEntity<?> download(BinaryContentResult binaryContentResult);
}
