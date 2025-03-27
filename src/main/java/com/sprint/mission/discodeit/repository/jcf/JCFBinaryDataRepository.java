package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.BinaryDataResponseDto;
import com.sprint.mission.discodeit.entity.BinaryData;
import com.sprint.mission.discodeit.repository.BinaryDataRepository;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
@Repository
@RequiredArgsConstructor
public class JCFBinaryDataRepository implements BinaryDataRepository {

    private final FileManager fileManager;

    @Override
    public BinaryDataResponseDto save(BinaryData binaryData) {
        return fileManager.writeToFile(binaryData);
    }
}
