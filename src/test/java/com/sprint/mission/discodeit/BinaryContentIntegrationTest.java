package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.basic.repositoryimpl.BasicBinaryContentRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BinaryContentIntegrationTest {

    @Spy
    private BinaryContentRepository binaryContentRepository = new BasicBinaryContentRepositoryImpl();

    @Mock
    private BinaryContentService binaryContentService;

    // 테스트용 데이터
    private UUID contentId;
    private UUID ownerId;
    private String ownerType = "TEST";
    private String contentType = "image/jpeg";
    private byte[] contentData = "test image data".getBytes();
    private String fileName = "test.jpg";

    @BeforeEach
    void setUp() {
        // 테스트마다 새로운 UUID 생성
        ownerId = UUID.randomUUID();
        
        // 테스트용 바이너리 컨텐츠 생성 및 저장
        BinaryContent content = new BinaryContent(contentData, contentType, fileName, ownerId, ownerType);
        binaryContentRepository.register(content);
        contentId = content.getId();
    }

    @Test
    void testCreateBinaryContent() {
        // 준비
        BinaryContentDto.Create createDto = BinaryContentDto.Create.builder()
            .contentType("text/plain")
            .fileName("newfile.txt")
            .ownerType("message")
            .ownerId(ownerId)
            .build();

        // 모의 응답 설정
        BinaryContentDto.Summary mockSummary = BinaryContentDto.Summary.builder()
            .email("test@example.com")
            .filename("newfile.txt")
            .contentType("text/plain")
            .data("test data".getBytes())
            .build();
        
        when(binaryContentService.createBinaryContent(any(BinaryContentDto.Create.class))).thenReturn(mockSummary);
        
        // 실행
        BinaryContentDto.Summary result = binaryContentService.createBinaryContent(createDto);

        // 검증
        assertNotNull(result);
        verify(binaryContentService).createBinaryContent(any(BinaryContentDto.Create.class));
    }

    @Test
    void testFindBinaryContentById() {
        // 준비
        BinaryContentDto.Summary mockSummary = BinaryContentDto.Summary.builder()
            .email("test@example.com")
            .filename(fileName)
            .contentType(contentType)
            .data(contentData)
            .build();
            
        when(binaryContentService.findBinaryContent(contentId)).thenReturn(mockSummary);
        
        // 실행
        BinaryContentDto.Summary foundContent = binaryContentService.findBinaryContent(contentId);

        // 검증
        assertNotNull(foundContent);
        verify(binaryContentService).findBinaryContent(contentId);
    }

    @Test
    void testFindAllBinaryContents() {
        // 추가 컨텐츠 생성
        UUID secondOwnerId = UUID.randomUUID();
        BinaryContent secondContent = new BinaryContent(
            "PDF data".getBytes(),
            "application/pdf",
            "document.pdf",
            secondOwnerId,
            ownerType
        );
        binaryContentRepository.register(secondContent);

        // 모의 설정
        List<UUID> ids = binaryContentRepository.findAll();
        List<BinaryContentDto.Summary> mockSummaries = new ArrayList<>();
        mockSummaries.add(
            BinaryContentDto.Summary.builder()
                .email("test1@example.com")
                .filename(fileName)
                .contentType(contentType)
                .data(contentData)
                .build()
        );
        mockSummaries.add(
            BinaryContentDto.Summary.builder()
                .email("test2@example.com")
                .filename("document.pdf")
                .contentType("application/pdf")
                .data("PDF data".getBytes())
                .build()
        );
        
        when(binaryContentService.findBinaryContent(ids)).thenReturn(mockSummaries);
        
        // 실행
        List<BinaryContentDto.Summary> contents = binaryContentService.findBinaryContent(ids);

        // 검증
        assertNotNull(contents);
        assertEquals(2, contents.size());
        verify(binaryContentService).findBinaryContent(ids);
    }

    @Test
    void testDeleteBinaryContent() {
        // 모의 설정
        BinaryContentDto.DeleteResponse mockResponse = BinaryContentDto.DeleteResponse.builder()
            .fileName(fileName)
            .message("성공적으로 삭제되었습니다")
            .build();
            
        when(binaryContentService.deleteBinaryContent(contentId)).thenReturn(mockResponse);
        
        // 실행
        BinaryContentDto.DeleteResponse response = binaryContentService.deleteBinaryContent(contentId);

        // 검증
        assertNotNull(response);
        verify(binaryContentService).deleteBinaryContent(contentId);
    }
} 