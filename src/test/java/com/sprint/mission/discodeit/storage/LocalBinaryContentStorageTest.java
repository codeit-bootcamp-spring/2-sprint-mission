package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.binarycontent.storage.LocalBinaryContentStorage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;

class LocalBinaryContentStorageTest {

    private BinaryContentStorage binaryContentStorage;
    @TempDir
    private Path tempDirPath;

    @BeforeEach
    void setUp() {
        binaryContentStorage = new LocalBinaryContentStorage(tempDirPath);
    }

    @DisplayName("바이너리 컨텐츠 파일의 ID와 바이트 코드를 저장합니다.")
    @Test
    void putTest() {
        // given
        UUID binaryContentId = UUID.randomUUID();

        // when
        UUID binaryContentID = binaryContentStorage.put(binaryContentId, "hello".getBytes());

        // then
        assertAll(
                () -> Assertions.assertThat(binaryContentID).isEqualTo(binaryContentId),
                () -> Assertions.assertThat(getFileBytes(tempDirPath.resolve(binaryContentID.toString()))).isEqualTo("hello".getBytes())
        );

    }

    @DisplayName("바이너리 컨텐츠를 저장할떄 아이디가 null 이면, 예외를 반환합니다.")
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
    void getTest() throws IOException {
        // given
        UUID binaryContentId = UUID.randomUUID();
        binaryContentStorage.put(binaryContentId, "hello".getBytes());

        // when
        InputStream inputStream = binaryContentStorage.get(binaryContentId);

        // then
        Assertions.assertThat(inputStream.readAllBytes()).isEqualTo("hello".getBytes());

    }

    @DisplayName("바이너리 컨텐츠를 다운로드 하면, ~이다")
    @Test
    void downloadTest() throws IOException {
        // given
        UUID binaryContentId = UUID.randomUUID();
        binaryContentStorage.put(binaryContentId, "hello".getBytes());
        BinaryContentResult binaryContentResult = new BinaryContentResult(binaryContentId, null, "", "");

        // when
        InputStreamResource download = binaryContentStorage.download(binaryContentResult);

        // then
        Assertions.assertThat(download.getInputStream().readAllBytes())
                .isEqualTo("hello".getBytes());
    }

    private byte[] getFileBytes(Path filePath) {
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new UncheckedIOException("파일을 읽을 수 없습니다: " + filePath, e);
        }
    }

}