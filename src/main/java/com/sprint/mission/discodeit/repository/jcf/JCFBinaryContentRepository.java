package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
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
    public Optional<byte[]> findBinaryById(UUID id) {
        return Optional.of(data.get(id).getFileData());
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> idList) {
        return data.values().stream()
                .filter(binaryContent -> idList.contains(binaryContent.getId()))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public List<byte[]> findAll() {
        List<byte[]> fileData = data.values().stream()
                .map(BinaryContent::getFileData)
                .toList();

        return new ArrayList<>(fileData);
    }


}
