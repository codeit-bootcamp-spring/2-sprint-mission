package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryDataResponseDto;
import com.sprint.mission.discodeit.dto.SaveBinaryContentParamDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryData;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.BinaryDataRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryDataRepository binaryDataRepository;
    private final FileManager fileManager;

    @Override
    public BinaryContent save(SaveBinaryContentParamDto saveBinaryContentParamDto) {
        String originalFileName = saveBinaryContentParamDto.fileName();
        byte[] binaryData = saveBinaryContentParamDto.fileData();
        BinaryDataResponseDto binaryDataResponseDto = binaryDataRepository.save(new BinaryData(originalFileName, binaryData));
        BinaryContent binaryContent = new BinaryContent(binaryDataResponseDto.filePath(), binaryDataResponseDto.fileName(), originalFileName);
        binaryContentRepository.save(binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID binaryContentUUID) {
        return binaryContentRepository.findById(binaryContentUUID)
                .orElseThrow(() -> new NoSuchElementException("파일을 찾는데 실패하였습니다."));
    }

    @Override
    public List<BinaryContent> findByIdIn(List<UUID> binaryContentUUIDList) {
        return binaryContentRepository.findAll().stream()
                .filter(binaryContent -> binaryContentUUIDList.contains(binaryContent.getId()))
                .toList();
    }

    @Override
    public void delete(UUID userStatusUUID) {
        binaryContentRepository.delete(userStatusUUID);
    }
}
