package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.file.FileNotFoundCustomException;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;
    private static final Logger log = LoggerFactory.getLogger(BasicBinaryContentService.class);

    @Transactional
    @Override
    public BinaryContentDto create(BinaryContentCreateRequest biRequest) {
        String fileName = biRequest.fileName();
        long fileSize = (biRequest.bytes() != null) ? biRequest.bytes().length : 0;
        log.info("파일 메타데이터 생성 및 저장 시작. 파일명: '{}', 크기: {} bytes", fileName, fileSize);

        if (biRequest.bytes() == null || biRequest.bytes().length == 0) {
            log.warn("파일 메타데이터 생성 실패 (내용 없음): 파일명: '{}'", fileName);
            throw new FileProcessingCustomException("create", fileName, "파일 내용이 비어있어 메타데이터를 생성할 수 없습니다.");
        }
        
        BinaryContent binaryContent = new BinaryContent(
            fileName,
            biRequest.contentType(),
            fileSize // 위에서 계산된 값 사용
        );

        BinaryContent savedMetadata = binaryContentRepository.save(binaryContent);
        UUID metadataId = savedMetadata.getId();
        log.info("파일 메타데이터 DB 저장 완료. ID: '{}', 파일명: '{}'", metadataId, fileName);

        try {
            // 실제 파일 저장 로직 (binaryContentStorage 사용)
            binaryContentStorage.put(metadataId, biRequest.bytes());
            log.info("실제 파일 저장 완료. 저장소 ID: '{}' (메타데이터 ID와 동일)", metadataId);
        } catch (FileProcessingCustomException e) {
            log.error("실제 파일 저장 중 처리 오류: 메타데이터 ID '{}', 파일명 '{}'. 오류: {}", metadataId, fileName, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("실제 파일 저장 중 예기치 않은 오류: 메타데이터 ID '{}', 파일명 '{}'", metadataId, fileName, e);
            throw new FileProcessingCustomException("create-storage", fileName, "파일 저장 중 예상치 못한 오류 발생: " + e.getMessage());
        }

        return binaryContentMapper.toDto(savedMetadata);
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        log.info("파일 메타데이터 조회 시도. ID: '{}'", binaryContentId);
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(() -> {
                log.warn("파일 메타데이터 조회 실패 (찾을 수 없음): ID '{}'", binaryContentId);
                return new FileNotFoundCustomException(binaryContentId.toString(), "파일 메타데이터를 찾을 수 없습니다.");
            });
        log.info("파일 메타데이터 조회 성공. ID: '{}', 파일명: '{}'", binaryContentId, binaryContent.getFileName());
        return binaryContent;
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        if (CollectionUtils.isEmpty(binaryContentIds)) {
            log.info("조회할 파일 메타데이터 ID 목록이 비어있음.");
            return new ArrayList<>();
        }
        log.info("ID 목록으로 파일 메타데이터 조회 시도. ID 개수: {}", binaryContentIds.size());
        List<BinaryContent> contents = binaryContentRepository.findAllByIdIn(binaryContentIds);
        log.info("ID 목록으로 파일 메타데이터 조회 완료. 조회된 개수: {}", contents.size());
        return contents.stream().toList();
    }

    @Override
    @Transactional
    public void delete(UUID binaryContentId) {
        log.info("파일 메타데이터 및 실제 파일 삭제 시작. ID: '{}'", binaryContentId);
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(() -> {
                log.warn("파일 메타데이터 삭제 실패 (찾을 수 없음): ID '{}'", binaryContentId);
                return new FileNotFoundCustomException(binaryContentId.toString(), "삭제할 파일 메타데이터를 찾을 수 없습니다.");
            });

        // 실제 파일 삭제
        try {
            binaryContentStorage.delete(binaryContentId);
            log.info("실제 파일 삭제 완료 (저장소). 저장소 ID: '{}'", binaryContentId);
        } catch (FileNotFoundCustomException e) {
            log.warn("실제 파일 삭제 중 파일 없음 (저장소): ID '{}', 파일명 '{}'. 상세: {}. 메타데이터 삭제는 계속 진행.",
                binaryContentId, binaryContent.getFileName(), e.getMessage());
        } catch (FileProcessingCustomException e) {
            log.error("실제 파일 삭제 중 처리 오류 (저장소): ID '{}', 파일명 '{}'. 상세: {}. 메타데이터 삭제는 계속 진행.",
                binaryContentId, binaryContent.getFileName(), e.getMessage(), e);
        } catch (Exception e) { 
            log.error("실제 파일 삭제 중 예기치 않은 오류 (저장소): ID '{}', 파일명 '{}'. 메타데이터 삭제는 계속 진행.",
                binaryContentId, binaryContent.getFileName(), e);
        }
        
        // 메타데이터 삭제
        binaryContentRepository.deleteById(binaryContentId);
        log.info("파일 메타데이터 DB 삭제 완료. ID: '{}'", binaryContentId);
    }
}
