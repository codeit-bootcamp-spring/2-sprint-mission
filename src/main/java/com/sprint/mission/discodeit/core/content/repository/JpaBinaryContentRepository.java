package com.sprint.mission.discodeit.core.content.repository;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

  Iterable<UUID> id(UUID id);

  List<BinaryContent> findAllByIdIn(Collection<UUID> ids);
}
