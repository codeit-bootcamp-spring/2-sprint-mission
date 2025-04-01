package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryData;
import com.sprint.mission.discodeit.repository.BinaryDataRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
@Repository
public class JCFBinaryDataRepository implements BinaryDataRepository {

    Map<UUID, BinaryData> binaryDataList = new HashMap<>();

    @Override
    public BinaryData save(BinaryData binaryData) {
        binaryDataList.put(binaryData.getId(), binaryData);
        return binaryDataList.get(binaryData.getId());
    }

    @Override
    public Optional<BinaryData> findById(UUID binaryContentUUID) {
        return Optional.of(binaryDataList.get(binaryContentUUID));
    }
}
