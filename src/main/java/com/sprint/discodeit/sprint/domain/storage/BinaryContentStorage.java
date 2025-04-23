package com.sprint.discodeit.sprint.domain.storage;

import com.sprint.discodeit.sprint.domain.dto.binaryContentDto.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

    Long put(Long id, byte[] data);
    InputStream get(Long id);
    ResponseEntity<?> download(BinaryContentDto dto);
}
