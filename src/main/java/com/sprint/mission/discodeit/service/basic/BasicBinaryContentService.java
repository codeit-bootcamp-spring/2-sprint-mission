package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.custom.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(BinaryContentCreateDto binaryContentCreateDto) {
        BinaryContent binaryContent = new BinaryContent(binaryContentCreateDto.fileName(),
                (long) binaryContentCreateDto.bytes().length,
                binaryContentCreateDto.contentType(), binaryContentCreateDto.bytes());

        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent findById(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId);
        if (binaryContent == null) {
            throw new BinaryContentNotFoundException(binaryContentId + " binary content를 찾을 수 없습니다.");
        }

        return binaryContent;
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAll().stream()
                .filter(binaryContent ->
                        binaryContentIds.contains(binaryContent.getId())
                )
                .toList();
    }

    @Override
    public void delete(UUID binaryContentId) {
        binaryContentRepository.delete(binaryContentId);
    }
}
