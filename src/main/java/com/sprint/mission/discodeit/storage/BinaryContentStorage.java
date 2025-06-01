package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {
    UUID put(UUID binaryContentId, byte[] bytes);

    UUID delete(UUID binaryContentId);

    ResponseEntity<?> download(BinaryContentDto binaryContentDto);
}
