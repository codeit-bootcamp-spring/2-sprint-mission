package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.*;
import java.util.UUID;
public interface FileBinaryContentRepository {
        BinaryContent store(InputStream inputStream, String contentType, String originalFileName, long size, UUID ownerId, String ownerType) throws IOException;

        Optional<BinaryContent> findMetadataById(UUID id);

        Optional<InputStream> getContentStream(UUID id) throws IOException;

        boolean deleteById(UUID id);

        List<BinaryContent> findAllMetadataByOwnerId(UUID ownerId);

        Optional<BinaryContent> findMetadataByOwnerId(UUID ownerId);

        List<UUID> findAllIds();

}
