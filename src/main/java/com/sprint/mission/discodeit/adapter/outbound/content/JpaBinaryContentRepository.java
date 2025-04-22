package com.sprint.mission.discodeit.adapter.outbound.content;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

  Iterable<UUID> id(UUID id);
}
