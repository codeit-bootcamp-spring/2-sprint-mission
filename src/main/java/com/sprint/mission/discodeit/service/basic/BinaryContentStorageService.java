package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
            BinaryContent savedBinaryContent = binaryContentRepository.save(new BinaryContent(binaryContentRequest.fileName(), binaryContentRequest.contentType()));
            binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());

            binaryContents.add(savedBinaryContent);
        }

        return binaryContents;
    }

    public void deleteBinaryContentsBatch(List<UUID> attachmentIds) {
        binaryContentRepository.deleteAllByIdInBatch(attachmentIds);

        // TODO: 5/7/25 스토리지에 있는 것도 삭제 되어야한다
    }

}
