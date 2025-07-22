package com.sprint.mission.discodeit.core.storage.repository;

import com.sprint.mission.discodeit.core.storage.dto.BinaryContentDto;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStoragePort {

  void put(UUID id, byte[] bytes) throws IOException;

  ResponseEntity<?> download(BinaryContentDto binaryContentDto) throws FileNotFoundException;
}
