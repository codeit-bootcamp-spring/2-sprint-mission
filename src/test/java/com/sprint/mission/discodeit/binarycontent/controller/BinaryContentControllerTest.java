package com.sprint.mission.discodeit.binarycontent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.binarycontent.controller.BinaryContentController;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = BinaryContentController.class)
class BinaryContentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private BinaryContentService binaryContentService;
    @MockitoBean
    private BinaryContentStorage binaryContentStorage;
    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @DisplayName("이미지를 업로드시 바이너리 컨텐츠를 반환한다.")
    @Test
    void createProfileImage() {
        // given
        String name = UUID.randomUUID().toString();
        BinaryContent binaryContent = new BinaryContent(name, "jpg", 0);

        BinaryContentResult stubResult = BinaryContentResult.fromEntity(binaryContent);
        BDDMockito.given(binaryContentService.createBinaryContent(any())).willReturn(stubResult);

        // when & then
        Assertions.assertThat(mockMvc.post()
                        .uri("/api/binaryContents")
                        .multipart()
                        .file("multipartFile", "multipartFile".getBytes()))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.name")
                .isEqualTo(name);
    }

    @DisplayName("null을 업로드할 경우, 404 예외를 반환한다.")
    @Test
    void createProfileImage_NullException() {
        // given
        String name = UUID.randomUUID().toString();
        BinaryContent binaryContent = new BinaryContent(name, "jpg", 0);

        BinaryContentResult stubResult = BinaryContentResult.fromEntity(binaryContent);
        BDDMockito.given(binaryContentService.createBinaryContent(any())).willReturn(stubResult);

        // when & then
        Assertions.assertThat(mockMvc.post()
                        .uri("/api/binaryContents")
                        .multipart())
                .hasStatus4xxClientError();
    }

}