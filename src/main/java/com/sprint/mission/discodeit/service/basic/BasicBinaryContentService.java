package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @Override
    @Transactional
    public BinaryContentDto create(BinaryContentDto dto) {
        String fileName = dto.getFileName();
        String contentType = dto.getContentType();
        byte[] bytes = dto.getBytes();

        BinaryContent binaryContent = BinaryContent.
            createBinaryContent(fileName, (long) bytes.length, contentType);

        BinaryContent savedContent = binaryContentRepository.save(binaryContent);
        UUID binaryContentId = savedContent.getId();

        binaryContentStorage.put(binaryContentId, bytes);

        return binaryContentMapper.toDto(savedContent);
    }

    @Override
    @Transactional
    public BinaryContentDto findById(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id).orElse(null);
        if (binaryContent == null) {
            throw new NoSuchElementException("[ERROR] binary content not found");
        }

        return binaryContentMapper.toDto(binaryContent);
    }

    @Override
    @Transactional
    public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContents =
            binaryContentRepository.findAllById(binaryContentIds);

        return binaryContents.stream()
            .map(binaryContentMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        binaryContentStorage.deleteById(id);
        binaryContentRepository.deleteById(id);
    }
}
