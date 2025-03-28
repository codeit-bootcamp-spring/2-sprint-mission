package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryDataResponseDto;
import com.sprint.mission.discodeit.dto.SaveBinaryContentParamDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryData;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.BinaryDataRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
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

    @Override
    public BinaryContent save(SaveBinaryContentParamDto saveBinaryContentParamDto) {
        String originalFileName = saveBinaryContentParamDto.fileName();
        String contentType = saveBinaryContentParamDto.contentType();
        byte[] binaryData = saveBinaryContentParamDto.fileData();

        String extension =originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + "." + extension;

        BinaryDataResponseDto binaryDataResponseDto = binaryDataRepository.save(new BinaryData(fileName, binaryData));
        BinaryContent binaryContent = new BinaryContent(
                binaryDataResponseDto.filePath(),
                binaryDataResponseDto.fileName(),
                originalFileName,
                contentType
        );
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
