package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface BinaryContentService {

  BinaryContent create(BinaryContentCreateRequestDTO binaryContentCreateRequestDTO);

  BinaryContent findById(UUID binaryId);

  List<BinaryContent> findAllByIdIn();

  void delete(UUID binaryId);
}
