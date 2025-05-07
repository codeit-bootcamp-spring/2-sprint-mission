package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
            log.warn("파일 메타데이터 생성 실패: 파일 내용이 비어있습니다. 파일명: '{}'", fileName);
            // 혹은 여기서 예외를 던질 수도 있습니다. 현재는 빈 메타데이터라도 생성은 시도합니다 (size=0).
            // throw new IllegalArgumentException("파일 내용이 비어있으면 메타데이터를 생성할 수 없습니다.");
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
        } catch (Exception e) {
            log.error("메타데이터 ID '{}', 파일명 '{}'의 실제 파일 저장 중 오류 발생", metadataId, fileName, e);
            // 이 경우, 이미 저장된 메타데이터의 롤백 여부를 결정해야 합니다.
            // RuntimeException을 던지면 트랜잭션에 의해 메타데이터 저장이 롤백됩니다.
            throw new RuntimeException("실제 파일 저장 중 오류 발생: " + e.getMessage(), e);
        }

        return binaryContentMapper.toDto(savedMetadata);
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        log.info("파일 메타데이터 조회 시도. ID: '{}'", binaryContentId);
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(() -> {
                log.warn("ID '{}'에 해당하는 파일 메타데이터를 찾을 수 없습니다.", binaryContentId);
                // 메시지를 "유저가 존재하지 않음"에서 "파일 메타데이터를 찾을 수 없음"으로 수정
                return new NoSuchElementException("ID '" + binaryContentId + "'에 해당하는 파일 메타데이터를 찾을 수 없습니다.");
            });
        log.info("파일 메타데이터 조회 성공. ID: '{}', 파일명: '{}'", binaryContentId, binaryContent.getFileName());
        return binaryContent;
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        if (CollectionUtils.isEmpty(binaryContentIds)) {
            log.info("조회할 파일 메타데이터 ID 목록이 비어있습니다.");
            return new ArrayList<>();
        }
        log.info("{}개의 ID에 해당하는 파일 메타데이터 목록 조회 시도.", binaryContentIds.size());
        List<BinaryContent> contents = binaryContentRepository.findAllByIdIn(binaryContentIds);
        log.info("총 {}개의 파일 메타데이터를 조회했습니다.", contents.size());
        return contents.stream().toList(); // Java 16+ .toList()
    }

    @Override
    @Transactional
    public void delete(UUID binaryContentId) {
        log.info("파일 메타데이터 및 실제 파일 삭제 시작. ID: '{}'", binaryContentId);
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(() -> {
                log.warn("ID '{}'에 해당하는 삭제할 파일 메타데이터를 찾을 수 없습니다.", binaryContentId);
                return new NoSuchElementException("ID '" + binaryContentId + "'에 해당하는 파일 메타데이터를 찾을 수 없습니다.");
            });

        // 실제 파일 삭제 (BinaryContentStorage에 delete 메소드 추가 후 주석 해제 필요)
        /*
        try {
            binaryContentStorage.delete(binaryContentId); // 인터페이스에 delete(UUID id) 메소드 필요
            log.info("실제 파일 삭제 완료. 저장소 ID: '{}'", binaryContentId);
        } catch (Exception e) {
            log.error("ID '{}', 파일명 '{}'의 실제 파일 삭제 중 오류 발생. 메타데이터 삭제는 계속 진행합니다.",
                binaryContentId, binaryContent.getFileName(), e);
        }
        */
        log.warn("실제 파일 삭제 로직은 BinaryContentStorage.delete() 구현 후 활성화 필요. 현재는 메타데이터만 삭제됩니다. ID: '{}'", binaryContentId);
        
        // 메타데이터 삭제
        binaryContentRepository.deleteById(binaryContentId);
        log.info("파일 메타데이터 DB 삭제 완료. ID: '{}'", binaryContentId);
    }
}
