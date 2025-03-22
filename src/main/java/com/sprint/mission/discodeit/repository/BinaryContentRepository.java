package com.sprint.mission.discodeit.repository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface BinaryContentRepository {

    BinaryContent getBinaryContentByUserId(UUID userId);
    void delete(UUID binaryContentId);
}
