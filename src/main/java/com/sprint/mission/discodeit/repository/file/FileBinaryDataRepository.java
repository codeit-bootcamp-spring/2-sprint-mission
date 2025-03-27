package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.BinaryDataResponseDto;
import com.sprint.mission.discodeit.entity.BinaryData;
import com.sprint.mission.discodeit.repository.BinaryDataRepository;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileBinaryDataRepository implements BinaryDataRepository {

    private final FileManager fileManager;

    @Override
    public BinaryDataResponseDto save(BinaryData binaryData) {
        return fileManager.writeToFile(binaryData);
    }
}
