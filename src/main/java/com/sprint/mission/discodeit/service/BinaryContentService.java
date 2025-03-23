package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import org.springframework.context.event.EventListener;

import java.util.*;
import java.util.UUID;


public interface BinaryContentService {
    BinaryContentDto.Summary findBinaryContent(UUID id);
    BinaryContentDto.Summary createBinaryContent(BinaryContentDto.Create binaryContentDto);
    List<BinaryContentDto.Summary> findBinaryContent(List<UUID> ids);
    void deleteBinaryContent(List<UUID> binaryContentIds);
    BinaryContentDto.DeleteResponse deleteBinaryContent(UUID id);
    List<BinaryContentDto.DeleteResponse> deleteBinaryContents(List<UUID> ids);
    void deleteAllByOwnerId(UUID ownerId);
    void deleteAllByUserId(UUID userId);
}
