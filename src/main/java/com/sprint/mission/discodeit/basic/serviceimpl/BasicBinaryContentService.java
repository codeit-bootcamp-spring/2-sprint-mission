package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapping.FileMapping;
import com.sprint.mission.discodeit.service.BinaryContentRepository;
import com.sprint.mission.discodeit.service.MessageRepository;
import com.sprint.mission.discodeit.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@Service
public class BasicBinaryContentService implements com.sprint.mission.discodeit.service.BinaryContentService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public BasicBinaryContentService(
            @Qualifier("basicUserRepository") UserRepository userRepository,
            @Qualifier("basicBinaryContentRepository") BinaryContentRepository binaryContentRepository,
            @Qualifier("basicMessageRepository") MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.binaryContentRepository = binaryContentRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public BinaryContentDto.Summary createBinaryContent(BinaryContentDto.Create binaryContentDto) {
        // 소유자 엔티티 존재 검증
        UUID ownerId = binaryContentDto.getOwnerId();
        validateOwner(ownerId, binaryContentDto.getOwnerType());
        
        // 파일 데이터 변환
        byte[] fileData = byteFromFile(binaryContentDto.getFile());
        
        // BinaryContent 생성
        BinaryContent binaryContent = new BinaryContent(
                fileData,
                binaryContentDto.getContentType(),
                binaryContentDto.getFileName(), 
                ownerId, 
                binaryContentDto.getOwnerType()
        );

        // 저장
        binaryContentRepository.register(binaryContent);
        
        // 소유자 엔티티 업데이트 (필요한 경우)
        if (binaryContentDto.getOwnerType().equals("PROFILE")) {
            userRepository.findByUser(ownerId).ifPresent(user -> {
                user.setProfileId(binaryContent.getId());
                userRepository.updateUser(user);
            });
        } else if (binaryContentDto.getOwnerType().equals("MESSAGE")) {
            messageRepository.findById(ownerId).ifPresent(message -> {
                message.addAttachment(binaryContent.getId());
                messageRepository.updateMessage(message);
            });
        }
        
        return FileMapping.INSTANCE.binaryContentToSummary(binaryContent);
    }

    @Override
    public List<BinaryContentDto.Summary> findBinaryContent(List<UUID> ids) {
        List<BinaryContentDto.Summary> summaries = new ArrayList<>();
        for (UUID id : ids) {
            BinaryContent content = binaryContentRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("바이너리 콘텐츠를 찾을 수 없습니다: " + id));
            summaries.add(FileMapping.INSTANCE.binaryContentToSummary(content));
        }
        return summaries;
    }

    @Override
    public BinaryContentDto.Summary findBinaryContent(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("바이너리 콘텐츠를 찾을 수 없습니다: " + id));
        return FileMapping.INSTANCE.binaryContentToSummary(binaryContent);
    }

    public BinaryContentDto.DeleteResponse deleteBinaryContent(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("바이너리 콘텐츠를 찾을 수 없습니다: " + id));
        
        boolean deleted = binaryContentRepository.delete(id);
        String message = deleted ? "성공적으로 삭제되었습니다" : "삭제 실패";
        
        return FileMapping.INSTANCE.binaryContentToDeleteResponse(binaryContent, message);
    }
    
    public List<BinaryContentDto.DeleteResponse> deleteBinaryContents(List<UUID> ids) {
        List<BinaryContentDto.DeleteResponse> responses = new ArrayList<>();
        
        for (UUID id : ids) {
            try {
                // 객체 조회
                BinaryContent binaryContent = binaryContentRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("바이너리 콘텐츠를 찾을 수 없습니다: " + id));
                    
                // UUID로 삭제
                boolean deleted = binaryContentRepository.delete(id);
                String message = deleted ? "성공적으로 삭제되었습니다" : "삭제 실패";
                
                // 응답 객체 생성
                BinaryContentDto.DeleteResponse response = BinaryContentDto.DeleteResponse.builder()
                    .fileName(binaryContent.getFileName())
                    .message(message)
                    .build();
                
                responses.add(response);
            } catch (NoSuchElementException e) {
               throw new NoSuchElementException(e.getMessage());
            }
        }
        
        return responses;
    }
    
    public void deleteAllByOwnerId(UUID ownerId) {
        List<BinaryContent> contents = binaryContentRepository.findAllByOwnerId(ownerId);
        for (BinaryContent content : contents) {
            binaryContentRepository.delete(content.getId());
        }
    }

    public void deleteAllByUserId(UUID userId) {
        deleteAllByOwnerId(userId);
    }

    @Override
    public void deleteBinaryContent(List<UUID> binaryContentIds) {
        if (binaryContentIds != null && !binaryContentIds.isEmpty()) {
            deleteBinaryContents(binaryContentIds);
        }
    }

    private byte[] byteFromFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.getSize() == 0) {
            throw new RuntimeException("파일 데이터가 비어 있습니다.");
        }
        try {
            return multipartFile.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("파일 변환에 실패했습니다.", e);
        }
    }

    private void validateOwner(UUID ownerId, String ownerType) {
        switch (ownerType) {
            case "MESSAGE":
                messageRepository.findById(ownerId)
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 메시지입니다"));
                break;
            case "PROFILE":
                userRepository.findByUser(ownerId)
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다"));
                break;
            default:
                throw new IllegalArgumentException("지원되지 않는 소유자 타입입니다: " + ownerType);
        }
    }
}


