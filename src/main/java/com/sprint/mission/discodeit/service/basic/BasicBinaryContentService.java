package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.FindBinaryContentRequestDto;
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
        String fileName = saveBinaryContentParamDto.fileName();
        String contentType = saveBinaryContentParamDto.contentType();
        byte[] data = saveBinaryContentParamDto.fileData();

        BinaryData binaryData = binaryDataRepository.save(new BinaryData(data));
        BinaryContent binaryContent = new BinaryContent(
                binaryData.getId(),
                fileName,
                contentType
        );
        binaryContentRepository.save(binaryContent);
        return binaryContent;
    }

    @Override
    public FindBinaryContentRequestDto findById(UUID binaryContentUUID) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentUUID)
                .orElseThrow(() -> new NoSuchElementException("메타 데이터 파일을 찾는데 실패하였습니다."));

        BinaryData binaryData = binaryDataRepository.findById(binaryContent.getId())
                .orElseThrow(() -> new NoSuchElementException("원본 데이터 파일을 찾는데 실패하였습니다."));

        return new FindBinaryContentRequestDto(
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryData.getData(),
                binaryContent.getCreatedAt()
        );
    }

    @Override
    public List<FindBinaryContentRequestDto> findByIdIn(List<UUID> binaryContentUUIDList) {
        return binaryContentRepository.findAll().stream()
                .filter(binaryContent -> binaryContentUUIDList.contains(binaryContent.getId()))
                .map(binaryContent -> findById(binaryContent.getId()))
                .toList();
    }

    @Override
    public void delete(UUID userStatusUUID) {
        binaryContentRepository.delete(userStatusUUID);
    }
}
