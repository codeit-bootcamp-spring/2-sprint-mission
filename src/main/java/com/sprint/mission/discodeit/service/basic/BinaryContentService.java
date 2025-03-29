package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
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

    public BinaryContent create(BinaryContentCreateRequest request) {
        BinaryContent binaryContent = new BinaryContent(request.fileName(), (long) request.bytes().length, request.contentType(), request.bytes());

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
        if (!binaryContentRepository.existsById(id)) {
            throw new NoSuchElementException("BinaryContent with id " + id + " not found");
        }
        binaryContentRepository.delete(id);
    }
}
