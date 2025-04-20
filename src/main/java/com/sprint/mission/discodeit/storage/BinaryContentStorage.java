package com.sprint.mission.discodeit.storage;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {
    UUID put(UUID uuid, byte[] bytes);
    InputStream get(UUID uuid);
    ResponseEntity<Resource> download(UUID uuid);
}
