package com.sprint.mission.discodeit.core.storage.repository;

import com.sprint.mission.discodeit.core.storage.dto.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStoragePort {

  UUID put(UUID id, byte[] bytes);

  InputStream get(UUID id);

  ResponseEntity<?> download(BinaryContentDto binaryContentDto);
}
