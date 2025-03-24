package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data = new HashMap<>();

    @Override
    public void save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findByUserId(UUID userId) {
        return data.values().stream().filter(content -> userId.equals(content.getUserId())).toList();
    }

    @Override
    public List<BinaryContent> findByMessageId(UUID messageId) {
        return data.values().stream().filter(content -> messageId.equals(content.getMessageId())).toList();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
