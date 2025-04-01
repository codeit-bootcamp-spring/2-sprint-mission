package com.sprint.mission.discodeit.core.content.port;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);

    BinaryContent findById(UUID binaryId);

    List<BinaryContent> findAllByIdIn();

    void delete( UUID binaryId);
}
