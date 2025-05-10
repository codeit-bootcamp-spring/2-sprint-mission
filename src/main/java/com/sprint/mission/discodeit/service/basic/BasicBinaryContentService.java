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
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

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
    public BinaryContentDto create(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("파일 메타데이터 생성 실패 (파일이 없거나 비어있음)");
            throw new FileProcessingCustomException("create", "업로드된 파일이 없거나 내용이 비어있습니다.");
        }

        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        String contentType = file.getContentType();

        log.info("파일 메타데이터 생성 및 저장 시작. 파일명: '{}', 크기: {} bytes, 타입: '{}'", fileName, fileSize,
            contentType);

        BinaryContent binaryContent = new BinaryContent(
            fileName,
            contentType,
            fileSize
        );

        BinaryContent savedMetadata = binaryContentRepository.save(binaryContent);
        UUID metadataId = savedMetadata.getId();
        log.info("파일 메타데이터 DB 저장 완료. ID: '{}', 파일명: '{}'", metadataId, fileName);

        try {
            binaryContentStorage.put(metadataId, file.getBytes());
            log.info("실제 파일 저장 완료. 저장소 ID: '{}' (메타데이터 ID와 동일)", metadataId);
        } catch (IOException e) {
            log.error("파일 바이트를 읽는 중 IOException 발생");
            throw new FileProcessingCustomException("create-storage",
                "파일 내용을 읽는 중 오류 발생: " + e.getMessage());
        } catch (FileProcessingCustomException e) {
            log.error("실제 파일 저장 중 처리 오류발생");
            throw e;
        } catch (Exception e) {
            log.error("실제 파일 저장 중 예기치 않은 오류");
            throw new FileProcessingCustomException("create-storage", fileName,
                "파일 저장 중 예상치 못한 오류 발생: " + e.getMessage());
        }

        return binaryContentMapper.toDto(savedMetadata);
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        log.info("파일 메타데이터 조회 시도. ID: '{}'", binaryContentId);
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(() -> {
                log.warn("파일 메타데이터 조회 실패 (찾을 수 없음): ID '{}'", binaryContentId);
                return new FileNotFoundCustomException(binaryContentId.toString(),
                    "파일 메타데이터를 찾을 수 없습니다.");
            });
        log.info("파일 메타데이터 조회 성공. ID: '{}', 파일명: '{}'", binaryContentId,
            binaryContent.getFileName());
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
                return new FileNotFoundCustomException(binaryContentId.toString(),
                    "삭제할 파일 메타데이터를 찾을 수 없습니다.");
            });

        // 실제 파일 삭제
        try {
            binaryContentStorage.delete(binaryContentId);
            log.info("실제 파일 삭제 완료 (저장소). 저장소 ID: '{}'", binaryContentId);
        } catch (FileNotFoundCustomException e) {
            log.warn("실제 파일 삭제 중 파일 없음 (삭제는 진행)");
        } catch (FileProcessingCustomException e) {
            log.error("실제 파일 삭제 중 예기치 않은 오류 (삭제는 진행)");
        } catch (Exception e) {
            log.error("실제 파일 삭제 중 예기치 않은 오류");
        }

        // 메타데이터 삭제
        binaryContentRepository.deleteById(binaryContentId);
        log.info("파일 메타데이터 DB 삭제 완료. ID: '{}'", binaryContentId);
    }
}
