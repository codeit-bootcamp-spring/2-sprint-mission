package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    private static final Map<UUID, List<BinaryContent>> binaryContentMap = new HashMap<>();

    @Override
    public void save() {
    }

    @Override
    public void addBinaryContent(BinaryContent content) {
        binaryContentMap.computeIfAbsent(content.referenceId(), k -> new ArrayList<>()).add(content);
    }

    @Override
    public BinaryContent findBinaryContentById(UUID id) {
        return binaryContentMap.values().stream()
                .flatMap(List::stream)
                .filter(content -> content.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public BinaryContent findBinaryContentByUserId(UUID referenceId) {
        return binaryContentMap.getOrDefault(referenceId, Collections.emptyList())
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BinaryContent> findBinaryContentByMessageId(UUID referenceId) {
        return binaryContentMap.getOrDefault(referenceId, Collections.emptyList());
    }

    @Override
    public List<BinaryContent> findAllBinaryContents() {
        return binaryContentMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBinaryContent(UUID binaryContentId) {
        binaryContentMap.values().forEach(list -> list.removeIf(content -> content.id().equals(binaryContentId)));
    }

    @Override
    public boolean existsBinaryContent(UUID binaryContentId) {
        return binaryContentMap.values().stream()
                .flatMap(List::stream)
                .anyMatch(content -> content.id().equals(binaryContentId));
    }
}
