package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.discodeit.util.FileUtils.getBytesFromMultiPartFile;
import static com.sprint.mission.discodeit.util.mock.file.MockFile.createMockImageFile;
import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.web.multipart.MultipartFile;


class FileBinaryContentRepositoryTest {

  private static final String BINARY_FILE = "binaryContent.ser";
  @TempDir
  private Path path;
  private BinaryContentRepository binaryContentRepository;
  private BinaryContent setUpBinaryContent;
  private BinaryContent savedSetUpBinaryContent;


  @BeforeEach
  void setUp() {
    binaryContentRepository = new FileBinaryContentRepository(path.resolve(BINARY_FILE));
    MultipartFile imageFile = createMockImageFile("dog.jpg");
    setUpBinaryContent = new BinaryContent(imageFile.getName(),
        imageFile.getContentType(),
        imageFile.getSize(), getBytesFromMultiPartFile(imageFile));

    savedSetUpBinaryContent = binaryContentRepository.save(setUpBinaryContent);
  }

  @DisplayName("바이너리 파일을 저장할 경우, 저장한 파일을 반환합니다.")
  @Test
  void save() {
    assertThat(setUpBinaryContent.getId()).isEqualTo(savedSetUpBinaryContent.getId());
  }

  @DisplayName("바이너리 파일 ID로 조화할 경우, 같은 ID를 가진 바이너리 파일을 반환합니다.")
  @Test
  void findByBinaryContentId() {
    Optional<BinaryContent> binaryContent = binaryContentRepository.findByBinaryContentId(
        savedSetUpBinaryContent.getId());

    assertThat(binaryContent).map(BinaryContent::getId)
        .hasValue(savedSetUpBinaryContent.getId());
  }

  @DisplayName("바이너리 파일을 삭제할 경우, 반환값은 없슽니다.")
  @Test
  void delete() {
    binaryContentRepository.delete(savedSetUpBinaryContent.getId());
    Optional<BinaryContent> binaryContent = binaryContentRepository.findByBinaryContentId(
        savedSetUpBinaryContent.getId());

    assertThat(binaryContent).isNotPresent();
  }
}