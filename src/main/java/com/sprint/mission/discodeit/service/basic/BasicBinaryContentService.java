package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResDto;
import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentReqDto;
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
    public BinaryContentResDto create(CreateBinaryContentReqDto createBinaryContentReqDto) {
        BinaryContent binaryContent = new BinaryContent(createBinaryContentReqDto.fileData());
        binaryContentRepository.save(binaryContent);
        return new BinaryContentResDto(binaryContent.getId());
    }

    @Override
    public BinaryContentResDto find(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found"));

        return new BinaryContentResDto(binaryContent.getId());
    }

    @Override
    public List<BinaryContentResDto> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllByIdIn(binaryContentIds)
                .stream()
                .map(binaryContent -> new BinaryContentResDto(binaryContent.getId())).toList();
    }

    @Override
    public void delete(UUID binaryContentId) {
        if (!binaryContentRepository.existsById(binaryContentId)) {
            throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
        }
        binaryContentRepository.deleteById(binaryContentId);
    }
}
