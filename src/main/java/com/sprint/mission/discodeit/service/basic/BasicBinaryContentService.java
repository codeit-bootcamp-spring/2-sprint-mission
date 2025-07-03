package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.file.FileNotFoundCustomException;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.BinaryContentStorage;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;
    private static final Logger log = LoggerFactory.getLogger(BasicBinaryContentService.class);

    @Transactional
    @Override
    public BinaryContentDto create(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.error("파일이 null이거나 비어있습니다");
            throw new FileProcessingCustomException();
        }

        String fileName = file.getOriginalFilename();
        Long size = file.getSize();
        String contentType = file.getContentType();
        
        BinaryContent binaryContent = BinaryContent.builder()
            .fileName(fileName)
            .size(size)
            .contentType(contentType)
            .build();

        BinaryContent savedMetadata = binaryContentRepository.save(binaryContent);
        UUID metadataId = savedMetadata.getId();

        try {
            byte[] fileBytes = file.getBytes();
            binaryContentStorage.put(metadataId, fileBytes);
            
            // 저장 후 실제로 파일이 존재하는지 검증
            try {
                binaryContentStorage.get(metadataId);
            } catch (Exception verifyEx) {
                binaryContentRepository.deleteById(metadataId);
                throw new FileProcessingCustomException();
            }
            
        } catch (FileProcessingCustomException e) {
            binaryContentRepository.deleteById(metadataId);
            throw e;
        } catch (IOException e) {
            binaryContentRepository.deleteById(metadataId);
            throw new FileProcessingCustomException();
        } catch (Exception e) {
            binaryContentRepository.deleteById(metadataId);
            throw new FileProcessingCustomException();
        }

        return binaryContentMapper.toDto(savedMetadata);
    }

    @Override
    public BinaryContentDto find(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(FileNotFoundCustomException::new);
        
        return binaryContentMapper.toDto(binaryContent);
    }

    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
        if (CollectionUtils.isEmpty(binaryContentIds)) {
            return new ArrayList<>();
        }
        List<BinaryContent> contents = binaryContentRepository.findAllByIdIn(binaryContentIds);
        return contents.stream()
            .map(binaryContentMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public void delete(UUID binaryContentId) {
        log.info("파일 삭제 시작");
        binaryContentRepository.findById(binaryContentId)
            .orElseThrow(FileNotFoundCustomException::new);

        // 실제 파일 삭제
        try {
            binaryContentStorage.delete(binaryContentId);
            log.info("실제 파일 삭제 완료");
        } catch (FileNotFoundCustomException e) {
            log.warn("실제 파일 삭제 중 파일 없음 (삭제는 진행)");
        } catch (FileProcessingCustomException e) {
            log.error("실제 파일 삭제 중 예기치 않은 오류 (삭제는 진행)");
        } catch (Exception e) {
            log.error("실제 파일 삭제 중 예기치 않은 일반 오류", e);
        }

        binaryContentRepository.deleteById(binaryContentId);
        log.info("파일 삭제 완료");
    }

    /**
     * 데이터베이스에는 있지만 실제 파일이 없는 BinaryContent 레코드들을 정리합니다. 관리자용 유틸리티 메서드입니다.
     */
    @Transactional
    public int cleanupOrphanedRecords() {
        log.info("🧹 고아 BinaryContent 레코드 정리 시작");

        List<BinaryContent> allRecords = binaryContentRepository.findAll();
        int cleanedCount = 0;

        for (BinaryContent record : allRecords) {
            try {
                // 실제 파일이 존재하는지 확인
                binaryContentStorage.get(record.getId());
                log.debug("파일 존재 확인 - ID: {}", record.getId());
            } catch (FileNotFoundCustomException e) {
                // 파일이 없는 경우 레코드 삭제
                log.warn("🧹 고아 레코드 삭제 - ID: {}, 파일명: {}", record.getId(), record.getFileName());
                binaryContentRepository.deleteById(record.getId());
                cleanedCount++;
            } catch (Exception e) {
                log.error("🧹 파일 확인 중 오류 - ID: {}", record.getId(), e);
            }
        }

        log.info("🧹 고아 BinaryContent 레코드 정리 완료 - 정리된 개수: {}", cleanedCount);
        return cleanedCount;
    }
}
