package com.sprint.mission.discodeit.basic.repositoryimpl;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository("basicBinaryContentRepository")
public class BasicBinaryContentRepositoryImpl implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> binaryContentMap = new HashMap<>();
    
    @Override
    public boolean register(BinaryContent binaryContent) {
        binaryContentMap.put(binaryContent.getId(), binaryContent);
        return true;
    }

    @Override
    public boolean update(BinaryContent binaryContent) {
        if (binaryContentMap.containsKey(binaryContent.getId())) {
            binaryContentMap.put(binaryContent.getId(), binaryContent);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(BinaryContent binaryContent) {
        return binaryContentMap.remove(binaryContent.getId()) != null;
    }

    @Override
    public List<UUID> findAll() {
        return new ArrayList<>(binaryContentMap.keySet());
    }

    @Override
    public List<BinaryContent> findAllByOwnerId(UUID ownerId) {
        return binaryContentMap.values().stream()
                .filter(content -> content.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(UUID id) {
        return binaryContentMap.remove(id) != null;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(binaryContentMap.get(id));
    }
}
