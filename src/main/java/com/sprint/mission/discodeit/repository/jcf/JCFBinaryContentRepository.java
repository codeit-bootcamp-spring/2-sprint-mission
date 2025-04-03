package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "jcf")
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findByFileName(String fileName) {
        return data.values().stream()
                .filter(content -> content.getFileName().equals(fileName)).toList();
    }

    @Override
    public List<BinaryContent> findByContentType(String contentType) {
        return data.values().stream()
                .filter(content -> content.getContentType().equals(contentType)).toList();
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        if (!data.containsKey(id)) {
            throw new NoSuchElementException(id + " not found");
        }
        data.remove(id);
    }

    @Override
    public List<BinaryContent> findAllByIds(List<UUID> ids) {
        return ids.stream().map(data::get).filter(Objects::nonNull).toList();
    }

}
