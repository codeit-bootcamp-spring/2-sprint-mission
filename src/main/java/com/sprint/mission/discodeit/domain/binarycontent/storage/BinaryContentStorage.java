package com.sprint.mission.discodeit.domain.binarycontent.storage;

import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {

    UUID put(UUID binaryContentId, byte[] bytes);

    InputStream get(UUID binaryContentId);

    InputStreamResource download(UUID binaryContentId);

}
