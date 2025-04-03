package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository extends Repository<BinaryContent> {
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
}
