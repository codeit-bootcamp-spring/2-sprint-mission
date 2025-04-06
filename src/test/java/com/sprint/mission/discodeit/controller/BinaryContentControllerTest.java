package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.util.FileUtils.getBytesFromMultiPartFile;
import static com.sprint.mission.discodeit.util.mock.file.FileInfo.IMAGE_NAME_DOG;
import static com.sprint.mission.discodeit.util.mock.file.FileInfo.IMAGE_NAME_KIRBY;
import static com.sprint.mission.discodeit.util.mock.file.MockFile.createMockImageFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = BinaryContentController.class)
class BinaryContentControllerTest {

    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private BinaryContentService binaryContentService;

    private BinaryContent binaryContent;
    private BinaryContentResult stubResult;

    @BeforeEach
    void setUp() {
        MultipartFile imageFile = createMockImageFile(IMAGE_NAME_DOG);
        binaryContent = new BinaryContent(imageFile.getName(), imageFile.getContentType(), imageFile.getSize(), getBytesFromMultiPartFile(imageFile));
        stubResult = BinaryContentResult.fromEntity(binaryContent);
    }

    @Test
    void createProfileImage() throws IOException {
        when(binaryContentService.createProfileImage(any())).thenReturn(stubResult);

        assertThat(mockMvc.post()
                .uri("/api/binaryContents")
                .multipart()
                .file("multipartFile", createMockImageFile(IMAGE_NAME_DOG).getBytes()))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(binaryContent.getId().toString());
    }

    @Test
    void getById() {
        when(binaryContentService.getById(any())).thenReturn(stubResult);

        assertThat(mockMvc.get().uri("/api/binaryContents/{binaryContentId}", binaryContent.getId()))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(binaryContent.getId().toString());
    }

    @Test
    void getByIdIn() {
        MultipartFile imageFile = createMockImageFile(IMAGE_NAME_KIRBY);
        UUID otherBinaryContentId = UUID.randomUUID();
        BinaryContentResult otherStubResult = new BinaryContentResult(otherBinaryContentId,
                Instant.now(),
                imageFile.getName(),
                imageFile.getContentType(),
                getBytesFromMultiPartFile(imageFile));

        when(binaryContentService.getByIdIn(any())).thenReturn(List.of(otherStubResult, stubResult));


        assertThat(mockMvc.get()
                .uri("/api/binaryContents")
                .queryParam("binaryContentIds", binaryContent.getId().toString(), otherBinaryContentId.toString()))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].id")
                .asList()
                .containsExactlyInAnyOrder(binaryContent.getId().toString(), otherBinaryContentId.toString());
    }

    @Test
    void delete() {
        UUID fileId = UUID.randomUUID();
        assertThat(mockMvc.delete().uri("/api/binaryContents/{binaryContentId}", fileId))
                .hasStatus(HttpStatus.NO_CONTENT);
    }
}