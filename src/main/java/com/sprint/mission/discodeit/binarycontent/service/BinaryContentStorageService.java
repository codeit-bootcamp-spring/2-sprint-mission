package com.sprint.mission.discodeit.binarycontent.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BinaryContentStorageService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    public List<BinaryContent> createBinaryContents(List<BinaryContentRequest> binaryContentRequests) {
        List<BinaryContent> binaryContents = new ArrayList<>();
        for (BinaryContentRequest binaryContentRequest : binaryContentRequests) {
            BinaryContent savedBinaryContent = binaryContentRepository.save(
                    new BinaryContent(binaryContentRequest.fileName(), binaryContentRequest.contentType()));

            binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());
            binaryContents.add(savedBinaryContent);
        }

        return binaryContents;
    }

    public void deleteBinaryContentsBatch(List<UUID> attachmentIds) {
        binaryContentRepository.deleteAllByIdInBatch(attachmentIds);
    }

}
