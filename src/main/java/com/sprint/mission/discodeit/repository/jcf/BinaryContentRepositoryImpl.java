package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BinaryContentRepositoryImpl implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> storage = new HashMap<>();

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return storage.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);

    }

    @Override
    public List<BinaryContent> findAllById(List<UUID> ids) {
        List<BinaryContent> result = new ArrayList<>();
        for (UUID id : ids) {
            if (storage.containsKey(id)) {
                result.add(storage.get(id));
            }
        }
        return result;
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        storage.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public void saveAll(List<BinaryContent> binaryContents) {
        for (BinaryContent content : binaryContents) {
            storage.put(content.getId(), content);
        }
    }

    @Override
    public void deleteByUserId(UUID userId) {
        storage.entrySet().removeIf(entry -> entry.getValue().getUserId().equals(userId));
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        storage.entrySet().removeIf(entry -> entry.getValue().getMessageId().equals(messageId));
    }
}