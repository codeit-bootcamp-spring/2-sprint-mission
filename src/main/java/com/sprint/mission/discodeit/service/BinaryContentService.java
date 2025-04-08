package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {

  BinaryContent save(MultipartFile file) throws IOException;

  BinaryContent findById(UUID binaryContentUUID);

  List<BinaryContent> findByIdIn(List<UUID> binaryContentUUIDList);

  void delete(UUID userStatusUUID);
}
