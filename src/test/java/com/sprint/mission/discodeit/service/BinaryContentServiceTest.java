package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static com.sprint.mission.discodeit.util.FileUtils.getBytesFromMultiPartFile;
import static com.sprint.mission.discodeit.util.mock.file.FileInfo.IMAGE_NAME_DOG;
import static com.sprint.mission.discodeit.util.mock.file.FileInfo.IMAGE_NAME_KIRBY;
import static com.sprint.mission.discodeit.util.mock.file.MockFile.createMockImageFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class BinaryContentServiceTest {

    private BinaryContentService binaryContentService;

    @BeforeEach
    void setUp() {
        binaryContentService = new BasicBinaryContentService(new JCFBinaryContentRepository());
    }

    @DisplayName("이미지 파일을 저장하면 올바른 경로가 반환된다.")
    @Test
    void createProfileImage() {
        MultipartFile file = createMockImageFile(IMAGE_NAME_DOG);
        BinaryContentResult binaryContentResult = binaryContentService.createProfileImage(file);

        assertThat(binaryContentResult.bytes()).isEqualTo(getBytesFromMultiPartFile(file));
    }

    @DisplayName("여러 ID로 조회하면 해당하는 모든 바이너리 콘텐츠가 반환된다.")
    @Test
    void findAllByIdIn() {
        MultipartFile imageFile = createMockImageFile(IMAGE_NAME_DOG);
        BinaryContentResult binaryContentResult = binaryContentService.createProfileImage(imageFile);

        MultipartFile otherImageFile = createMockImageFile(IMAGE_NAME_KIRBY);
        BinaryContentResult otherBinaryContentResult = binaryContentService.createProfileImage(
                otherImageFile);

        assertAll(
                () -> assertThat(binaryContentResult.bytes()).isEqualTo(
                        getBytesFromMultiPartFile(imageFile)),
                () -> assertThat(otherBinaryContentResult.bytes()).isEqualTo(
                        getBytesFromMultiPartFile(otherImageFile))
        );
    }

    @DisplayName("이미지를 삭제하면 로컬 저장소에서도 삭제된다.")
    @Test
    void deleteProfileImage() {
        MultipartFile file = createMockImageFile(IMAGE_NAME_DOG);
        BinaryContentResult binaryContentResult = binaryContentService.createProfileImage(file);

        binaryContentService.delete(binaryContentResult.id());

        assertThatThrownBy(() -> binaryContentService.getById(binaryContentResult.id()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}