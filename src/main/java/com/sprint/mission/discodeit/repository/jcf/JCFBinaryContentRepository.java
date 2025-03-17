package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> storage = new ConcurrentHashMap<>();

    @Override
    public BinaryContent save(BinaryContent entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<BinaryContent> findAllById(List<UUID> ids) {
        return StreamSupport.stream(ids.spliterator(), false)
                .map(storage::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}