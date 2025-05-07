package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDownload;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {


  BinaryContentDto create(BinaryContentCreateRequest binaryContentCreateRequest);

  BinaryContentDto findById(UUID id);

  List<BinaryContentDto> findAllByIds(List<UUID> ids);

  void delete(UUID id);

  @Transactional(readOnly = true)
  BinaryContentDownload download(UUID binaryContentId) throws IOException;
}
