package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    public BinaryContent create(BinaryContentCreateRequest request) {
        String fileName = request.fileName();
        byte[] bytes = request.bytes();
        String contentType = request.contentType();
        BinaryContent binaryContent = BinaryContent.builder()
                .fileName(fileName)
                .size((long) bytes.length)
                .contentType(contentType)
                .build();

        binaryContent = binaryContentRepository.save(binaryContent);
        binaryContentStorage.put(binaryContent.getId(), bytes);
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
                .toList();
    }

    @Override
    public void delete(UUID binaryContentId) {
        if (!binaryContentRepository.existsById(binaryContentId)) {
            throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
        }
        binaryContentRepository.deleteById(binaryContentId);
    }

    @Override
    public ResponseEntity<?> download(UUID binaryContentId) {
        BinaryContent binaryContent = find(binaryContentId);
        BinaryContentDto dto = new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType()
        );
        return binaryContentStorage.download(dto);
    }
}
