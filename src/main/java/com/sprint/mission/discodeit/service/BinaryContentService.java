package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.UUID;


public interface BinaryContentService {


  BinaryContentDto.Summary createBinaryContent(BinaryContentDto.Upload binaryContentDto)
      throws IOException;

  void writeFilesAsZip(List<UUID> ids, OutputStream os) throws IOException;

  BinaryContentDto.Summary findBinaryContentSummary(UUID id);

  List<BinaryContentDto.Summary> findBinaryContentSummariesByIds(List<UUID> ids);

  BinaryContent getBinaryContentEntity(UUID id);

  Optional<InputStream> getContentStream(UUID id) throws IOException;

  void deleteBinaryContentByOwner(UUID ownerId);

  List<BinaryContentDto.DeleteResponse> deleteBinaryContentsByIds(List<UUID> ids);
}
