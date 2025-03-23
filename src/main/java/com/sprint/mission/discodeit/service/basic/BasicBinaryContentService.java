package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.BinaryContentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void createBinaryContent(BinaryContentCreateDto createDto) {
        binaryContentRepository.createBinaryContent(createDto.convertCreateDtoToBinaryContent());
    }

    @Override
    public BinaryContentResponseDto findById(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id);
        return BinaryContentResponseDto.convertToResponseDto(binaryContent);
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> idList) {
        List<BinaryContent> binaryContentList = binaryContentRepository.findAllByIdIn(idList);
        return binaryContentList.stream()
                .map(BinaryContentResponseDto::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBinaryContent(UUID id) {
        binaryContentRepository.deleteBinaryContent(id);
    }

    @Override
    public List<byte[]> findAll() {
        return binaryContentRepository.findAll();
    }

}
