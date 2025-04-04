package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final HashMap<UUID, BinaryContent> binaryContents = new HashMap<>();

    @Override
    public void save(BinaryContent binaryContent) {
        binaryContents.put(binaryContent.getId(), binaryContent);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(binaryContents.get(id));
    }

    @Override
    public Optional<List<BinaryContent>> findAll() {
        return Optional.of(new ArrayList<>(binaryContents.values()));
    }

    @Override
    public Optional<List<BinaryContent>> findAllByIdIn(UUID userId) {
        return Optional.of(new ArrayList<>(binaryContents.values()).stream().filter(binaryContent -> binaryContent.getUserId().equals(userId)).collect(Collectors.toList()));
    }

    @Override
    public void update(BinaryContent binaryContent) {
        save(binaryContent);
    }

    @Override
    public void delete(UUID id) {
        binaryContents.remove(id);
    }
}
