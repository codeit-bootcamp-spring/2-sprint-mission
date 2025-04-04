package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {

  UUID createBinaryContent(MultipartFile profile);

  BinaryContent findBinaryContent(UUID binaryContentId);

  List<BinaryContent> findAllBinaryContent();

  void deleteBinaryContent(UUID binaryContentId);
}
