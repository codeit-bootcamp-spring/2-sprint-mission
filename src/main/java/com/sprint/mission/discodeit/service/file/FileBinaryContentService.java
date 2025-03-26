package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void create(CreateBinaryContentRequest binaryContentRequest) {
        BinaryContent binaryContent = new BinaryContent(binaryContentRequest.getBinaryImage());

        binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent find(UUID id) {
        return binaryContentRepository.find(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAll().stream()
            .filter(bc -> binaryContentIds.contains(bc.getId())).toList();
    }

    @Override
    public void delete(UUID contentId) {
        binaryContentRepository.delete(contentId);
    }
}
