package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    Map<UUID, BinaryContent> data = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        data.put(binaryContent.getUuid(), binaryContent);

        return binaryContent;
    }

    @Override
    public BinaryContent findByKey(UUID key) {
        return data.get(key);
    }

    @Override
    public List<BinaryContent> findAllByKeys(List<UUID> binaryKeys) {
        return data.values().stream()
                .filter(binaryContent -> binaryKeys.contains(binaryContent.getUuid()))
                .toList();
    }

    @Override
    public boolean existsByKey(UUID binaryKey) {
        return data.containsKey(binaryKey);
    }

    @Override
    public void delete(UUID key) {
        data.remove(key);
    }
}
