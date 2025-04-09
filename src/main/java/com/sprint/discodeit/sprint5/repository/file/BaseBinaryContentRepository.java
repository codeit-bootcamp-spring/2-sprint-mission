package com.sprint.discodeit.sprint5.repository.file;

import com.sprint.discodeit.sprint5.domain.entity.BinaryContent;
import com.sprint.discodeit.sprint5.repository.util.AbstractFileRepository;
import com.sprint.discodeit.sprint5.repository.util.FilePathUtil;
import com.sprint.discodeit.sprint5.repository.util.SaveRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class BaseBinaryContentRepository extends AbstractFileRepository<BinaryContent> implements SaveRepository<BinaryContent> {


    protected BaseBinaryContentRepository() {
        super(FilePathUtil.BINARY.getPath());
    }

    @Override
    public void save(BinaryContent binaryContent) {
        Map<UUID, BinaryContent> binaryContentMap = loadAll();
        if (binaryContentMap.containsKey(binaryContent.getId())) {
            throw new IllegalArgumentException("[DEBUG] 동일한 UUID의 데이터가 이미 존재하므로 추가하지 않음: " + binaryContent.getId());
        } else {
            binaryContentMap.put(binaryContent.getId(), binaryContent);
            writeToFile(binaryContentMap);
        }
    }

    @Override
    public void delete(UUID binaryContentId) {
        Map<UUID, BinaryContent> binaryContentMap = loadAll();
        binaryContentMap.remove(binaryContentId);
        writeToFile(binaryContentMap);
    }


    public Optional<BinaryContent> findById(UUID binaryContentId) {
        Map<UUID, BinaryContent> binaryContentMap = loadAll();
        return Optional.ofNullable(binaryContentMap.get(binaryContentId));
    }

    public List<BinaryContent> findByAll() {
        Map<UUID, BinaryContent> binaryContentMap = loadAll();
        return binaryContentMap.values().stream().toList();
    }
}
