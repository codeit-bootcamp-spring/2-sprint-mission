package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.core.storage.controller.dto.BinaryContentDto;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.core.storage.repository.JpaBinaryContentRepository;
import com.sprint.mission.discodeit.core.storage.repository.S3BinaryContentStorage;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.http.SdkHttpRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ExtendWith(MockitoExtension.class)
public class S3BinaryContentStorageTest {

  @Mock
  private S3Client s3Client;

  @Mock
  private S3Presigner s3Presigner;

  @Mock
  private JpaBinaryContentRepository binaryContentRepository;

  @InjectMocks
  private S3BinaryContentStorage binaryContentStorage;

  @Test
  void put_Success() {
    // given
    UUID fakeId = UUID.randomUUID();
    BinaryContent binaryContent = BinaryContent.create("test.png", 1L, "image/png");

    when(binaryContentRepository.findById(fakeId)).thenReturn(Optional.of(binaryContent));
    // when
    UUID result = binaryContentStorage.put(fakeId, new byte[0]);
    // then
    assertEquals(result, fakeId);
  }

  @Test
  void put_WithoutBinaryContent_ShouldThrowException() {
    // given
    when(binaryContentRepository.findById(any())).thenReturn(Optional.empty());
    // when & then
    assertThrows(BinaryContentNotFoundException.class, () -> {
      binaryContentStorage.put(UUID.randomUUID(), new byte[0]);
    });
  }

  @Test
  void get_Success() {
    // given
    UUID fakeId = UUID.randomUUID();
    BinaryContent binaryContent = BinaryContent.create("test.png", 1L, "image/png");

    when(binaryContentRepository.findById(fakeId)).thenReturn(Optional.of(binaryContent));
    // when & then
    binaryContentStorage.get(fakeId);
  }

  @Test
  void get_WithoutBinaryContent_ShouldThrowException() {
    // given
    when(binaryContentRepository.findById(any())).thenReturn(Optional.empty());
    // when & then
    assertThrows(BinaryContentNotFoundException.class, () -> {
      binaryContentStorage.get(UUID.randomUUID());
    });
  }

  @Test
  void download_Success() {
    // given
    UUID fakeId = UUID.randomUUID();
    BinaryContentDto dto = new BinaryContentDto(fakeId, "test.png", 1L, "image/png");

    PresignedGetObjectRequest presignedRequest = PresignedGetObjectRequest.builder()
        .httpRequest(
            SdkHttpRequest.builder().uri(URI.create("https://mock-s3.com/test.png")).method(
                SdkHttpMethod.GET).build())
        .expiration(Instant.now().plusSeconds(600))
        .isBrowserExecutable(true)
        .signedHeaders(Map.of("Host", List.of("mock-s3.com")))
        .build();

    when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
        .thenReturn(presignedRequest);
    // when & then
    ResponseEntity<?> response = binaryContentStorage.download(dto);
    assertEquals(HttpStatus.FOUND, response.getStatusCode());
    assertEquals("https://mock-s3.com/test.png", response.getHeaders().getLocation().toString());
  }
}
