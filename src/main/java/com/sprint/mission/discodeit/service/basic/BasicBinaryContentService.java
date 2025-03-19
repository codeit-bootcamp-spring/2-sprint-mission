package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.SaveBinaryContentParamDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    BinaryContentRepository binaryContentRepository;

    @Override
    public void save(SaveBinaryContentParamDto saveBinaryContentParamDto) {
        BinaryContent binaryContent = new BinaryContent(saveBinaryContentParamDto.image());
        binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent findById(UUID binaryContentUUID) {
        return binaryContentRepository.findById(binaryContentUUID)
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public List<BinaryContent> findByIdIn(List<UUID> binaryContentUUIDList) {
        return binaryContentRepository.findAll().stream()
                .filter(binaryContent -> binaryContentUUIDList.contains(binaryContent.getId()))
                .toList();
    }

    @Override
    public void delete(UUID userStatusUUID) {
        binaryContentRepository.delete(userStatusUUID);
    }
}
