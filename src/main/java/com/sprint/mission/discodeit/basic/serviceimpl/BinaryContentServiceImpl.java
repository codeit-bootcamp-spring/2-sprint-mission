package com.sprint.mission.discodeit.basic.serviceimpl;


import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.InvalidRequestException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapping.FileMapping;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Service
@RequiredArgsConstructor // final 필드 생성자 자동 주입
public class BinaryContentServiceImpl implements BinaryContentService {

    private final UserRepository userRepository;
    private final FileBinaryContentRepository binaryContentRepository;
    private final MessageRepository messageRepository;

    @Override
    public BinaryContentDto.Summary createBinaryContent(BinaryContentDto.Upload uploadDto) throws IOException {
        UUID ownerId = uploadDto.getOwnerId();
        String ownerType = uploadDto.getOwnerType();
        MultipartFile file = uploadDto.getFile();

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        validateOwnerExists(ownerId, ownerType);

        try (InputStream inputStream = file.getInputStream()) {
            BinaryContent persistedMetadata = binaryContentRepository.store(
                    inputStream,
                    file.getContentType(),      // 파일의 MIME 타입
                    file.getOriginalFilename(), // 원본 파일 이름
                    file.getSize(),             // 파일 크기
                    ownerId,                    // 소유자 ID
                    ownerType                   // 소유자 타입/용도
            );

            return FileMapping.INSTANCE.binaryContentToSummary(persistedMetadata);

        } catch (IOException e) {

            throw new RuntimeException("파일 저장 처리 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public BinaryContentDto.Summary findBinaryContentSummary(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findMetadataById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BinaryContent", "id", id));
        return FileMapping.INSTANCE.binaryContentToSummary(binaryContent);
    }

    @Override
    public List<BinaryContentDto.Summary> findBinaryContentSummariesByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<BinaryContentDto.Summary> summaries = new ArrayList<>();
        for (UUID id : ids) {
            try {
                binaryContentRepository.findMetadataById(id)
                        .ifPresent(content -> summaries.add(FileMapping.INSTANCE.binaryContentToSummary(content)));
            } catch (Exception e) {
                System.err.printf("Warning: Error fetching BinaryContent summary for ID %s: %s. Skipping.%n", id, e.getMessage());
            }
        }
        return summaries;
    }

    @Override
    public BinaryContent getBinaryContentEntity(UUID id) {
        // 리포지토리의 findMetadataById 사용
        return binaryContentRepository.findMetadataById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BinaryContent", "id", id));
    }

    @Override
    public Optional<InputStream> getContentStream(UUID id) throws IOException {
        return binaryContentRepository.getContentStream(id);
    }

    @Override
    public void deleteBinaryContentByOwner(UUID ownerId) {

        List<BinaryContent> contentsToDelete = binaryContentRepository.findAllMetadataByOwnerId(ownerId);

        if (contentsToDelete.isEmpty()) {
            return;
        }

        List<UUID> failedToDeleteIds = new ArrayList<>();
        for (BinaryContent content : contentsToDelete) {
            UUID contentId = content.getId();
            try {
                if (!binaryContentRepository.deleteById(contentId)) {
                    failedToDeleteIds.add(contentId);
                }
            } catch (Exception e) {
                failedToDeleteIds.add(contentId);
            }
        }

        if (!failedToDeleteIds.isEmpty()) {
            throw new RuntimeException("일부 바이너리 컨텐츠 삭제에 실패했습니다: " + failedToDeleteIds);
        }
    }

    @Override
    public List<BinaryContentDto.DeleteResponse> deleteBinaryContentsByIds(List<UUID> ids) {
        List<BinaryContentDto.DeleteResponse> responses = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return responses;
        }

        for (UUID id : ids) {
            String fileName = "N/A"; // 기본값
            boolean success = false;
            String message = "";

            try {
                // 삭제 전 파일 이름을 얻기 위해 메타데이터 조회
                Optional<BinaryContent> contentOpt = binaryContentRepository.findMetadataById(id);
                if (contentOpt.isPresent()) {
                    fileName = contentOpt.get().getFileName();
                    if (binaryContentRepository.deleteById(id)) {
                        success = true;
                        message = "성공적으로 삭제되었습니다.";
                    } else {
                        success = false;
                        message = "삭제 실패 (파일이 없거나 권한 문제 등)";
                        System.err.printf("Warning: Deletion failed (repository returned false) for binary content ID: %s%n", id);
                    }
                } else {
                    // 삭제 대상 메타데이터가 처음부터 없음
                    success = true; // 성공으로 간주 (이미 없으므로)
                    message = "삭제 대상 없음 (ID: " + id + ")";
                    System.err.printf("Warning: Binary content metadata not found for ID: %s. Skipping deletion.%n", id);
                }
            } catch (Exception e) {
                // 삭제 작업 중 예외 발생
                success = false;
                message = "삭제 중 서버 오류 발생: " + e.getMessage();
                System.err.printf("Error during deletion of binary content ID %s: %s%n", id, e.getMessage());
            }

            // 응답 DTO 생성 및 리스트 추가
            responses.add(BinaryContentDto.DeleteResponse.builder()
                    .id(id)
                    .fileName(fileName)
                    .success(success)
                    .message(message)
                    .build());
        }
        return responses;
    }

    private void validateOwnerExists(UUID ownerId, String ownerType) {
        boolean exists;
        switch (ownerType) {
            case "MESSAGE":
                exists = messageRepository.findAllByAuthorId(ownerId).size()>0;
                if (!exists) {
                    throw new ResourceNotFoundException("Message (owner)", "id", ownerId);
                }
                break;
            case "PROFILE":
                exists = userRepository.findByUser(ownerId).isPresent();
                if (!exists) {
                    throw new ResourceNotFoundException("User (owner)", "id", ownerId);
                }
                break;
            default:
                throw new InvalidRequestException("owner_type", "지원되지 않는 소유자 타입입니다: " + ownerType);
        }
    }
}