package com.sprint.mission.discodeit.binarycontent.service.basic;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BinaryContentCore {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    public BinaryContent createBinaryContent(BinaryContentRequest binaryContentRequest) {
        if (binaryContentRequest == null) {
            return null;
        }
        BinaryContent savedBinaryContent = binaryContentRepository.save(new BinaryContent(binaryContentRequest.fileName(), binaryContentRequest.contentType()));
        binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());

        return savedBinaryContent;
    }

    @Transactional
    public List<BinaryContent> createBinaryContents(List<BinaryContentRequest> binaryContentRequests) {
        List<BinaryContent> binaryContents = new ArrayList<>();
        for (BinaryContentRequest binaryContentRequest : binaryContentRequests) {
            BinaryContent savedBinaryContent = binaryContentRepository.save(new BinaryContent(binaryContentRequest.fileName(), binaryContentRequest.contentType()));
            binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());
            binaryContents.add(savedBinaryContent);
        }

        return binaryContents;
    }

    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID의 바이너리 컨텐츠가 없습니다.");
        }

        binaryContentRepository.deleteById(id);
    }

}
