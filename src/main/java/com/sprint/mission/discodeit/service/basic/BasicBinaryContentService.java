package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import com.sprint.mission.discodeit.dto.file.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @Override
    @Transactional
    public BinaryContentDto create(CreateBinaryContentRequest request) {
        String filename = request.fileName();
        String contentType = request.contentType();
        byte[] bytes = request.bytes();

        BinaryContent binaryContent = new BinaryContent(filename, (long) bytes.length, contentType);
        BinaryContent saved = binaryContentRepository.save(binaryContent);
        UUID id = saved.getId();

        binaryContentStorage.put(id, bytes);
        return binaryContentMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BinaryContent> find(UUID id) {
        return binaryContentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllById(ids);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
