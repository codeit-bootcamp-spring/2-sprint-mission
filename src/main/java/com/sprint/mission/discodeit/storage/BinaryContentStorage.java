package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  UUID put(UUID BinaryContentId, byte[] data);

  InputStream get(UUID BinaryContentId);

  ResponseEntity<?> download(BinaryContentDto content);
}
