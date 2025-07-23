package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import io.micrometer.core.annotation.Timed;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {


    CompletableFuture<UUID> put(UUID binaryContentId, byte[] bytes);

    InputStream get(UUID binaryContentId);

    ResponseEntity<?> download(BinaryContentDto metaData);

    void recover(IOException e, BinaryContent content);
}
