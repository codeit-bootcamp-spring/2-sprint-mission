package com.sprint.mission.discodeit.binarycontent.service.basic;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.storage.BinaryContentStorage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
class BinaryContentCoreTest {

    @Autowired
    private BinaryContentCore binaryContentCore;
    @Autowired
    private BinaryContentStorage binaryContentStorage;
    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @Value("${discodeit.storage.local.root-path}")
    private Path rootPath;

    @AfterEach
    void tearDown() {
        if (Files.exists(rootPath)) {
            deleteRecursively(rootPath);
        }
    }

    @DisplayName("바이너리 컨텐츠를 생성합니다.")
    @Test
    void createBinaryContent() {
        // given
        String name = UUID.randomUUID().toString();
        BinaryContentRequest binaryContentRequest = new BinaryContentRequest(name, "", "hello".getBytes());

        // when
        BinaryContent binaryContent = binaryContentCore.createBinaryContent(binaryContentRequest);

        // then
        assertAll(

                () -> Assertions.assertThat(binaryContent.getFileName()).isEqualTo(name),
                () -> Assertions.assertThat(getFileBytes(binaryContent)).isEqualTo("hello".getBytes())
        );
    }


    @DisplayName("null 받을 경우, null을 리턴합니다.")
    @Test
    void createBinaryContent_Null() {
        // when
        BinaryContent binaryContent = binaryContentCore.createBinaryContent(null);

        // then
        Assertions.assertThat(binaryContent).isNull();
    }

    @DisplayName("여러번 바이너리 컨텐츠를 생성합니다.")
    @Test
    void createBinaryContents() {
        // given
        String firstName = UUID.randomUUID().toString();
        String secondName = UUID.randomUUID().toString();
        BinaryContentRequest firstBinaryContentRequest = new BinaryContentRequest(firstName, "", "hello".getBytes());
        BinaryContentRequest secondBinaryContentRequest = new BinaryContentRequest(secondName, "", "hello".getBytes());
        List<BinaryContentRequest> binaryContentRequests = List.of(firstBinaryContentRequest, secondBinaryContentRequest);

        // when
        List<BinaryContent> binaryContents = binaryContentCore.createBinaryContents(binaryContentRequests);

        // then
        Assertions.assertThat(binaryContents)
                .extracting(BinaryContent::getFileName)
                .containsExactlyInAnyOrder(firstName, secondName);
    }

    @DisplayName("바이너리 컨텐츠를 삭제합니다.")
    @Test
    void delete() {
        // given
        BinaryContentRequest binaryContentRequest = new BinaryContentRequest("", "", "hello".getBytes());
        BinaryContent binaryContent = binaryContentCore.createBinaryContent(binaryContentRequest);

        // when
        binaryContentCore.delete(binaryContent.getId());

        // then
        Assertions.assertThat(binaryContentRepository.findById(binaryContent.getId())).isNotPresent();
    }

    @DisplayName("바이너리 컨텐츠를 삭제시, 해당 객체가 없으면 삭제합니다.")
    @Test
    void delete_NoException() {
        // when & then
        Assertions.assertThatThrownBy(() -> binaryContentCore.delete(UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private byte[] getFileBytes(BinaryContent binaryContent) {
        try {
            return binaryContentStorage.get(binaryContent.getId()).readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void deleteRecursively(Path path) {
        File file = path.toFile();
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child.toPath());
                }
            }
        }

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to delete: " + path, e);
        }
    }
}