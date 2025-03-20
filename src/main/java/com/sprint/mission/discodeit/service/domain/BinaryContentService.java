package com.sprint.mission.discodeit.service.domain;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent create(BinaryContentDto request) {
        BinaryContent binaryContent = new BinaryContent(request.content(), request.contentType());

        return binaryContentRepository.save(binaryContent);
    }

    public BinaryContent find(UUID id){
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent does not exist"));
    }

    public List<BinaryContent> findAllByIdIn(List<UUID> attchmentIds){
        return attchmentIds.stream().map(id -> binaryContentRepository.findById(id)
                        .orElseThrow(()-> new NoSuchElementException("BinaryContent does not exist")))
                .toList();
    }

    public void delete(UUID id){
        binaryContentRepository.delete(id);
    }
}
