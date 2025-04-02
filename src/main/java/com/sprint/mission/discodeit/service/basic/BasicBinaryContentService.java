package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDto;
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
    public BinaryContent create(BinaryContentCreateRequest request) {
        String fileName = request.fileName();
        byte[] bytes = request.bytes();
        String contentType = request.contentType();
        BinaryContent binaryContent = new BinaryContent(
                fileName,
                contentType,
                bytes,
                (long) bytes.length
        );
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContentDto find(UUID id) {
        return binaryContentRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("Binary content not found with ID: " + id));
    }

    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIds(ids).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        if (binaryContentRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("Binary content not found with ID: " + id);
        }
        binaryContentRepository.deleteById(id);
    }

    public BinaryContent findRaw(UUID id){
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Binary content not found with ID: " + id));
    }

    private BinaryContentDto mapToDto(BinaryContent binaryContent) {
        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryContent.getBytes(),
                binaryContent.getCreatedAt()
        );
    }
}
