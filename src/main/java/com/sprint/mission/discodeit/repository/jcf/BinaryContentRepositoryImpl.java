package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BinaryContentRepositoryImpl implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> storage = new HashMap<>();

    @Override
    public BinaryContent findById(UUID id) {
        return storage.get(id);
    }

    @Override
    public void save(BinaryContent binaryContent) {
        storage.put(binaryContent.getId(), binaryContent);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        storage.entrySet().removeIf(entry -> entry.getValue().getUserId().equals(userId));
    }
}