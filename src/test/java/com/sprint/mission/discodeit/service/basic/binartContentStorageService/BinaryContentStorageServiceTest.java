package com.sprint.mission.discodeit.service.basic.binartContentStorageService;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.basic.BinaryContentStorageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("test")
@SpringBootTest
class BinaryContentStorageServiceTest {

    private static final String IMAGE_NAME_DOG = "dog";
    private static final String CONTENT_TYPE = "jpg";

    @Autowired
    private BinaryContentStorageService binaryContentStorageService;

    @DisplayName("바이너리 컨텐츠를 저장을 요청 받으면, 저장된 파일 정보를 반환합니다.")
    @Test
    void createBinaryContentsTest() {
        // given
        BinaryContentRequest binaryContentRequest = new BinaryContentRequest(IMAGE_NAME_DOG, CONTENT_TYPE, IMAGE_NAME_DOG.getBytes());
        List<BinaryContentRequest> binaryContentRequests = List.of(binaryContentRequest);

        // when
        List<BinaryContent> binaryContents = binaryContentStorageService.createBinaryContents(binaryContentRequests);

        // then
        Assertions.assertThat(binaryContents)
                .extracting(BinaryContent::getFileName, BinaryContent::getContentType)
                .containsExactlyInAnyOrder(tuple(IMAGE_NAME_DOG, CONTENT_TYPE));
    }

}