package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {

  BinaryContentResult createProfileImage(MultipartFile multipartFile);

  BinaryContentResult getById(UUID id);

  List<BinaryContentResult> getByIdIn(List<UUID> ids);

  void delete(UUID id);
}
