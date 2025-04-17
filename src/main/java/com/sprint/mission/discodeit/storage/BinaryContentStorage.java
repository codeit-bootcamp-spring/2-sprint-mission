package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  UUID put(UUID id, byte[] data);

  InputStream get(UUID id);

  ResponseEntity<Resource> download(BinaryContentDto contentDto);
}
