package com.sprint.mission.discodeit.repository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);

    BinaryContent findById(UUID binaryContentId);

    List<BinaryContent> findAll();

    void delete(UUID binaryContentId);
}
