package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    void save(BinaryContent binaryContent);

    BinaryContent find(UUID binaryId);

    List<BinaryContent> findAllByIdIn();

    boolean delete( UUID binaryId);
}
