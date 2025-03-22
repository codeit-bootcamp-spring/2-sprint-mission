package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final List<BinaryContent> binaryContentList = new ArrayList<>();

    @Override
    public void save(BinaryContent binaryContent) {
        binaryContentList.add(binaryContent);
    }

    @Override
    public Optional<BinaryContent> findById(UUID binaryContentUUID) {
        return binaryContentList.stream()
                .filter(binaryContent -> binaryContent.getId().equals(binaryContentUUID))
                .findAny();
    }

    @Override
    public List<BinaryContent> findAll() {
        return binaryContentList;
    }

    @Override
    public void delete(UUID binaryContentUUID) {
        binaryContentList.removeIf(binaryContent -> binaryContent.getId().equals(binaryContentUUID));
    }
}
