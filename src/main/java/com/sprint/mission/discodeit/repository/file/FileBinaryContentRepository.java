package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Primary
public class FileBinaryContentRepository extends AbstractFileRepository<Map<UUID, BinaryContent>> implements BinaryContentRepository {

    private Map<UUID, BinaryContent> data;

    public FileBinaryContentRepository() {
        super("BinaryContent");
        this.data = loadData();
    }

    @Override
    protected Map<UUID, BinaryContent> getEmptyData() {
        return new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
        saveData(data);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return ids.stream()
                .map(data::get)
                .toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        saveData(data);
    }
}
