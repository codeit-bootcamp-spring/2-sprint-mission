package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final HashMap<UUID, BinaryContent> binaryContentRepository = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContentRepository.put(binaryContent.getId(), binaryContent);

        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(binaryContentRepository.get(id));
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.remove(id);
    }
}
