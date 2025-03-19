package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.HashMap;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final HashMap<UUID, BinaryContent> binaryContentRepository = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContentRepository.put(binaryContent.getProfileId(), binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID id) {
        return binaryContentRepository.get(id);
    }
}
