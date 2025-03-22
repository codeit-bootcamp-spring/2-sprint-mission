package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    private static final JCFBinaryContentRepository instance = new JCFBinaryContentRepository();
    private final Map<UUID, BinaryContent> data = new HashMap<>();

    private JCFBinaryContentRepository() {}

    public static JCFBinaryContentRepository getInstance() {
        return instance;
    }

    @Override
    public void save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
    }

    @Override
    public Optional<BinaryContent> getById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> getAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public List<BinaryContent> getByUserId(UUID userId) {
        return data.values().stream()
                .filter(file -> userId.equals(file.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BinaryContent> getByMessageId(UUID messageId) {
        return data.values().stream()
                .filter(file -> messageId.equals(file.getMessageId()))
                .collect(Collectors.toList());
    }
}
