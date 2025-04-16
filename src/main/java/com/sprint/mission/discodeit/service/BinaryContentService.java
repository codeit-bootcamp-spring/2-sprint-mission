package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {

  BinaryContent createBinaryContent(MultipartFile profile);

  BinaryContentDto findBinaryContent(UUID binaryContentId);

  List<BinaryContentDto> findAllBinaryContent();

  void deleteBinaryContent(UUID binaryContentId);
}
