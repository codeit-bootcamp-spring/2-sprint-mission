package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateBinaryContentDTO;
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

    public BinaryContent create(CreateBinaryContentDTO createBinaryContentDTO){
        BinaryContent binaryContent = new BinaryContent(
                UUID.randomUUID(),
                createBinaryContentDTO.getContent(),
                createBinaryContentDTO.getContentType(),
                createBinaryContentDTO.getUserId(),
                createBinaryContentDTO.getMessageId()
        );
        binaryContentRepository.save(binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent find(UUID id) {
        return binaryContentRepository.findByUserId(id)
                .orElseThrow(()-> new NoSuchElementException("Binary Content with id " + id + " not found"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllById(ids);
    }

    @Override
    public void delete(UUID id) {
        BinaryContent binaryContent = find(id);
        binaryContentRepository.delete(binaryContent);
    }
}
