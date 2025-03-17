package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BinaryContentService {
    BinaryContent create();
    BinaryContent find(UUID binaryId);
    List<BinaryContent> findAllByIdIn();
    boolean delete(UUID binaryId);
}
