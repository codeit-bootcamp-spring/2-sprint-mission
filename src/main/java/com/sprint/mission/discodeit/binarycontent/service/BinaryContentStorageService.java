package com.sprint.mission.discodeit.binarycontent.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.user.entity.User;
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

        // TODO: 5/17/25 SaveAll로 수정바람
        for (BinaryContentRequest binaryContentRequest : binaryContentRequests) {
            BinaryContent savedBinaryContent = binaryContentRepository.save(new BinaryContent(binaryContentRequest.fileName(), binaryContentRequest.contentType()));
            binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());
            binaryContents.add(savedBinaryContent);
        }

        return binaryContents;
    }

    @Transactional
    public BinaryContent createBinaryContent(BinaryContentRequest binaryContentRequest) {
        if (binaryContentRequest == null) {
            return null;
        }

        BinaryContent savedBinaryContent = binaryContentRepository.save(new BinaryContent(binaryContentRequest.fileName(), binaryContentRequest.contentType()));
        binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());

        return savedBinaryContent;
    }

    public void deleteBinaryContentsBatch(List<UUID> attachmentIds) {
        binaryContentRepository.deleteAllByIdInBatch(attachmentIds);
    }

    public void deleteBinaryContent(User user) {
        if (user.getBinaryContent() == null) {
            return;
        }
        binaryContentRepository.deleteById(user.getBinaryContent().getId());
    }

}
