package com.sprint.mission.discodeit.domain.binarycontent.controller;

import static org.mockito.ArgumentMatchers.any;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.testutil.ControllerTestSupport;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class BinaryContentControllerTest extends ControllerTestSupport {



  @DisplayName("이미지를 업로드시 바이너리 컨텐츠를 반환한다.")
  @Test
  void createProfileImage() {
    // given
    String name = UUID.randomUUID().toString();
    BinaryContent binaryContent = new BinaryContent(name, "jpg", 0);

    BinaryContentResult stubResult = BinaryContentResult.fromEntity(binaryContent);
    BDDMockito.given(binaryContentService.createBinaryContent(any())).willReturn(stubResult);

    // when & then
    Assertions.assertThat(
            mockMvc.post()
                .uri("/api/binaryContents")
                .multipart()
                .file("multipartFile", "multipartFile".getBytes())
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
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