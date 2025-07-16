package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.async.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;

    @Override
    @Transactional(readOnly = true)
    public BinaryContentDto findById(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(
                () -> BinaryContentNotFoundException.forId(binaryContentId.toString()));
        return binaryContentMapper.toDto(binaryContent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentDto> findByIdIn(List<UUID> binaryContentIdList) {
        return binaryContentRepository.findByIdIn(binaryContentIdList).stream()
            .map(binaryContentMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public void updateStatus(UUID id, BinaryContentUploadStatus uploadStatus) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
            .orElseThrow(
                () -> BinaryContentNotFoundException.forId(id.toString()));

        binaryContent.updateStatus(uploadStatus);
        binaryContentRepository.save(binaryContent);
    }
}
