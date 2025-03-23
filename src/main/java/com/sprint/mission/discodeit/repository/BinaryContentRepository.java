package com.sprint.mission.discodeit.repository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface BinaryContentRepository {

    BinaryContent getBinaryContentByUserId(UUID userId);
    Map<UUID, BinaryContent> getBinaryContentData();
    void delete(UUID binaryContentId);
    List<BinaryContent> findAll();
    BinaryContent findById(UUID binaryContentId);
    BinaryContent save(BinaryContent binaryContent);
}
