package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.DTO.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    void save(BinaryContentCreateDTO binaryContentCreateDTO);

    BinaryContent find(UUID binaryId);

    List<BinaryContent> findAllByIdIn();

    boolean delete( UUID binaryId);
}
