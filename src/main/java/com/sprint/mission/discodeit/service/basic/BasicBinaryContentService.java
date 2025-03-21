package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(BinaryContentDto binaryContentDto) {
        String fileName = binaryContentDto.fileName();
        byte[] fileData = binaryContentDto.fileData();
        Long fileSize = (long) fileData.length;
        String contentType = binaryContentDto.contentType();

        BinaryContent binaryContent = new BinaryContent(fileName, fileData, fileSize, contentType);
        binaryContentRepository.save(binaryContent);

        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new NoSuchElementException("해당 파일의 정보를 찾을 수 없습니다."));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllByIdIn(binaryContentIds).stream().toList();
    }

    @Override
    public void delete(UUID binaryContentId) {
        if (!binaryContentRepository.existsById(binaryContentId)) {
            throw new NoSuchElementException("해당 파일의 정보를 찾을 수 없습니다.");
        }

        binaryContentRepository.deleteById(binaryContentId);
    }
}
