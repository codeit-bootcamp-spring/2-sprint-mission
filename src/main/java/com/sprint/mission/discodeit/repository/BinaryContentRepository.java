package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentRepository {
    String saveFile(BinaryContentType type, MultipartFile file, UUID id);

    BinaryContent save(BinaryContent content);

    Optional<BinaryContent> findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
