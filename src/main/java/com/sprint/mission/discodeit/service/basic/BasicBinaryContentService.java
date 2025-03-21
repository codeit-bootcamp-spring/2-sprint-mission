package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    public BinaryContentResponse create(BinaryContentCreateRequest request) {
        if (request.userId() != null && !userRepository.existsById(request.userId())) {
            throw new NoSuchElementException("User with id " + request.userId() + " not found");
        }
        if (request.messageId() != null && !messageRepository.existsById(request.messageId())) {
            throw new NoSuchElementException("Message with id " + request.messageId() + " not found");
        }

        BinaryContent binaryContent = new BinaryContent(request.userId(), request.messageId());
        binaryContentRepository.save(binaryContent);

        return new BinaryContentResponse(binaryContent.getId(), binaryContent.getUserId(), binaryContent.getMessageId(), null);
    }

    @Override
    public Optional<BinaryContentResponse> find(UUID id) {
        return binaryContentRepository.findById(id).map(content ->
                new BinaryContentResponse(content.getId(), content.getUserId(), content.getMessageId(), null));
    }

    @Override
    public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids) {
        return ids.stream()
                .map(binaryContentRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(content -> new BinaryContentResponse(content.getId(), content.getUserId(), content.getMessageId(), null))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        if (binaryContentRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("BinaryContent with id " + id + " not found");
        }
        binaryContentRepository.deleteById(id);
    }
}
