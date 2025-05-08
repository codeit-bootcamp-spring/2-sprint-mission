package com.sprint.mission.discodeit.service.basic;

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
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional(readOnly = true)
    public BinaryContentDto findById(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(
                () -> BinaryContentNotFoundException.forId(binaryContentId.toString()));
        InputStream is = binaryContentStorage.get(binaryContent.getId());
        return binaryContentMapper.toDto(binaryContent, is);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentDto> findByIdIn(List<UUID> binaryContentIdList) {
        return binaryContentRepository.findByIdIn(binaryContentIdList).stream()
            .map(binaryContent -> {
                InputStream is = binaryContentStorage.get(binaryContent.getId());
                return binaryContentMapper.toDto(binaryContent, is);
            })
            .toList();
    }
}
