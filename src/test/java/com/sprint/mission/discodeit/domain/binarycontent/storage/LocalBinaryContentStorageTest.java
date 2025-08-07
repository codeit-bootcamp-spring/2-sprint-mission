package com.sprint.mission.discodeit.domain.binarycontent.storage;

import com.sprint.mission.discodeit.domain.binarycontent.storage.local.LocalBinaryContentStorage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LocalBinaryContentStorageTest {

  private BinaryContentStorage binaryContentStorage;
  @TempDir
  private Path tempDirPath;

  @BeforeEach
  void setUp() {
    binaryContentStorage = new LocalBinaryContentStorage(tempDirPath);
  }

  @DisplayName("바이너리 컨텐츠 파일을 저장합니다.")
  @Test
  void putTest() throws ExecutionException, InterruptedException {
    // given
    UUID binaryContentId = UUID.randomUUID();
    byte[] fileContent = "hello".getBytes();

    // when
    UUID binaryContentID = binaryContentStorage.put(binaryContentId, fileContent).get();

    // then
    SoftAssertions.assertSoftly(softly -> {
          softly.assertThat(binaryContentID).isEqualTo(binaryContentId);
          softly.assertThat(getFileBytes(tempDirPath.resolve(binaryContentID.toString())))
              .isEqualTo(fileContent);
        }
    );
  }

  @DisplayName("입력값이 null 이면, 바이너리 컨텐츠 파일이 아닌 예외를 반환합니다.")
  @Test
  void putTest_NoIDException() {
    // when & then
    Assertions.assertThatThrownBy(() -> binaryContentStorage.put(null, "hello".getBytes()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("파일 저장시, 바이트코드가 null 이면, 예외를 반환합니다")
  @Test
  void putTest_NoBytesException() {
    // when & then
    Assertions.assertThatThrownBy(() -> binaryContentStorage.put(UUID.randomUUID(), null))
        .isInstanceOf(IllegalArgumentException.class);
  }


  @DisplayName("바이너리 컨텐츠의 ID로 조회하면, 해당 컨텐츠의 InputStream을 반환합니다.")
  @Test
  void getTest() throws IOException, ExecutionException, InterruptedException {
    // given
    UUID binaryContentId = UUID.randomUUID();
    byte[] fileBytes = "hello".getBytes();
    binaryContentStorage.put(binaryContentId, fileBytes).get();

    // when
    InputStream inputStream = binaryContentStorage.get(binaryContentId);

    // then
    Assertions.assertThat(inputStream.readAllBytes()).isEqualTo(fileBytes);
  }

  private byte[] getFileBytes(Path filePath) {
    try {
      return Files.readAllBytes(filePath);
    } catch (IOException e) {
      throw new UncheckedIOException("파일을 읽을 수 없습니다: " + filePath, e);
    }
  }

}