package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import com.sprint.mission.discodeit.dto.file.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.handler.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @Override
    @Transactional
    public BinaryContentDto create(CreateBinaryContentRequest request) {
        log.info("파일 업로드 요청 - filename: {}, contentType: {}", request.fileName(),
            request.contentType());

        String filename = request.fileName();
        String contentType = request.contentType();
        byte[] bytes = request.bytes();

        BinaryContent binaryContent = new BinaryContent(filename, (long) bytes.length, contentType);
        BinaryContent saved = binaryContentRepository.save(binaryContent);
        UUID id = saved.getId();

        binaryContentStorage.put(id, bytes);

        log.info("파일 업로드 완료 - fileId: {}", id);
        return binaryContentMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BinaryContentDto find(UUID id) {
        log.info("파일 단건 조회 요청 - fileId: {}", id);
        BinaryContent entity = binaryContentRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("파일 조회 실패 - 존재하지 않음 fileId: {} ", id);
                return new BinaryContentNotFoundException("파일을 찾을 수 없습니다.");
            });
        return binaryContentMapper.toDto(entity);

    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        log.info("파일 다중 조회 요청 - 개수: {}", ids.size());
        return binaryContentRepository.findAllById(ids);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.info("파일 삭제 요청 - fileId: {}", id);
        binaryContentRepository.deleteById(id);
        log.info("파일 삭제 완료 - fileId: {}", id);
    }
}
