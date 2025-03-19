package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent content, MultipartFile file);

    Optional<BinaryContent> findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
