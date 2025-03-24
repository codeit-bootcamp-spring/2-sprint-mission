package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> binaryContentMap;

    public JCFBinaryContentRepository() {
        this.binaryContentMap = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        this.binaryContentMap.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(binaryContentMap.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        List<BinaryContent> result = new ArrayList<>();
        for (UUID id : binaryContentIds) {
            BinaryContent content = binaryContentMap.get(id);
            if (content != null) {
                result.add(content);
            }
        }
        return result;
    }

    @Override
    public boolean existsById(UUID id) {
        return binaryContentMap.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        binaryContentMap.remove(id);
    }
}
