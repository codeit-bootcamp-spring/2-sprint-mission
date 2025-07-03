package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

    void put(UUID id, byte[] binaryContent);

    InputStream get(UUID id);

    ResponseEntity<Resource> download(BinaryContentDto binaryContentDto);

    void delete(UUID id);

}
