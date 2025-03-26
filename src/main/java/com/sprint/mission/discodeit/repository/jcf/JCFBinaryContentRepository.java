package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    // UUID를 key로 인메모리 저장소로 사용
    private final Map<UUID, BinaryContent> store = new ConcurrentHashMap<>();

    @Override
    public Optional<BinaryContent> findByUserId(UUID id) {
        return store.values().stream()
                .filter(bc -> bc.getUserId() != null && bc.getUserId().equals(id))
                .findFirst();
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<BinaryContent> findAllById(List<UUID> ids) {
        return store.entrySet().stream()
                .filter(entry -> ids.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public void save(BinaryContent binaryContent) {
        if (binaryContent.getId() == null) {
            binaryContent.setId(UUID.randomUUID());
        }
        store.put(binaryContent.getId(), binaryContent);
    }

    @Override
    public void delete(BinaryContent binaryContent) {
        store.remove(binaryContent.getId());
    }
}
