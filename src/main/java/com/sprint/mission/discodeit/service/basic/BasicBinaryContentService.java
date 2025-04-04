package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void create(BinaryContentDto binaryContentCreateDto) {
        binaryContentRepository.save(new BinaryContent(binaryContentCreateDto.id(), binaryContentCreateDto.userId(), binaryContentCreateDto.uploadFileName(), binaryContentCreateDto.storeFileName()));
    }

    @Override
    public BinaryContentDto find(UUID id) {
        return binaryContentRepository
                .findById(id)
                .map(BinaryContentDto::from)
                .orElseThrow(() -> new IllegalArgumentException("파일이 잘못되었습니다."));
    }

    @Override
    public List<BinaryContentDto> findAllByIdIn(UUID userId) {
        return binaryContentRepository
                .findAllByIdIn(userId)
                .orElseThrow(() -> new IllegalArgumentException("파일이 잘못되었습니다."))
                .stream()
                .map(BinaryContentDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.delete(id);
    }
}
