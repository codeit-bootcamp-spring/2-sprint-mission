package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  UUID put(UUID id, byte[] bytes) throws IOException;

  InputStream get(UUID id) throws IOException;

  ResponseEntity<?> download(BinaryContentDto binaryContentDto) throws IOException;

  void delete(UUID id) throws IOException;
}
