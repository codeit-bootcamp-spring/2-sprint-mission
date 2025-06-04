package com.sprint.mission.discodeit.repository;

import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

    UUID put(UUID id, byte[] binaryContent);

    InputStream get(UUID id);

    ResponseEntity<?> download(UUID id);

    void delete(UUID id);

}
