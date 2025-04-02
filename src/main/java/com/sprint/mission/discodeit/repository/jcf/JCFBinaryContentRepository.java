package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        name = "discordit.repository.type",
        havingValue = "jcf")
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private static final Map<UUID, BinaryContent> binaryContentMap = new HashMap<>();

    @Override
    public void save() {
    }

    @Override
    public void addBinaryContent(BinaryContent content) {
        binaryContentMap.put(content.getId(), content);
    }

    @Override
    public BinaryContent findBinaryContentById(UUID binaryContentId) {
        return binaryContentMap.get(binaryContentId);
    }

    @Override
    public List<BinaryContent> findAllBinaryContents() {
        return new ArrayList<>(binaryContentMap.values());
    }

    @Override
    public void deleteBinaryContentById(UUID binaryContentId) {
        binaryContentMap.remove(binaryContentId);
    }

    @Override
    public boolean existsBinaryContent(UUID binaryContentId) {
        return binaryContentMap.containsKey(binaryContentId);
    }
}
