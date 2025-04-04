package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.multipart.MultipartFile;

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
    private MockMvcTester mockMvcTester;
    @MockitoBean
    private BinaryContentService binaryContentService;

    private UUID binaryContentId;
    private BinaryContentResult stubResult;

    @BeforeEach
    void setUp() {
        MultipartFile imageFile = createMockImageFile(IMAGE_NAME_DOG);
        binaryContentId = UUID.randomUUID();
        stubResult = new BinaryContentResult(binaryContentId, imageFile.getName(),
                imageFile.getContentType(), imageFile.getSize(),
                getBytesFromMultiPartFile(imageFile));
    }

    @Test
    void createProfileImage() {
        when(binaryContentService.createProfileImage(any())).thenReturn(stubResult);

        assertThat(mockMvcTester.post().uri("/api/binary-contents"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(binaryContentId.toString());
    }

    @Test
    void getById() {
        when(binaryContentService.getById(any())).thenReturn(stubResult);

        assertThat(mockMvcTester.get().uri("/api/binary-contents/{fileId}", binaryContentId))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(binaryContentId.toString());
    }

    @Test
    void getByIdIn() {
        MultipartFile imageFile = createMockImageFile(IMAGE_NAME_KIRBY);
        UUID otherBinaryContentId = UUID.randomUUID();
        BinaryContentResult otherStubResult = new BinaryContentResult(otherBinaryContentId,
                imageFile.getName(),
                imageFile.getContentType(), imageFile.getSize(),
                getBytesFromMultiPartFile(imageFile));

        when(binaryContentService.getByIdIn(any())).thenReturn(List.of(otherStubResult, stubResult));


        assertThat(mockMvcTester.get()
                .queryParam("ids", binaryContentId.toString(), otherBinaryContentId.toString())
                .uri("/api/binary-contents"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].id")
                .asList()
                .containsExactlyInAnyOrder(binaryContentId.toString(), otherBinaryContentId.toString());
    }

    @Test
    void delete() {
        UUID fileId = UUID.randomUUID();
        assertThat(mockMvcTester.delete().uri("/api/binary-contents/{fileId}", fileId))
                .hasStatus(HttpStatus.NO_CONTENT);
    }
}