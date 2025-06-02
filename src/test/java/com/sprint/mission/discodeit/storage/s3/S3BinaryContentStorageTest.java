package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "discodeit.storage.type=s3"
})
public class S3BinaryContentStorageTest {

  @Autowired
  private BinaryContentStorage storage;

  @Test
  void putAndGetTest() throws Exception {
    UUID id = UUID.randomUUID();
    byte[] data = "sample data".getBytes();

    UUID savedId = storage.put(id, data);
    assertThat(savedId).isEqualTo(id);

    InputStream in = storage.get(savedId);
    assertThat(in.readAllBytes()).isEqualTo(data);
  }

  @Test
  void downloadTest() throws URISyntaxException {
    BinaryContentDto dto = BinaryContentDto.builder()
        .id(UUID.fromString("정상적으로 업로드된 ID 입력"))
        .fileName("test-file.txt")
        .contentType("text/plain")
        .size(1024L)
        .build();

    ResponseEntity<?> response = storage.download(dto);
    assertThat(response.getStatusCode().is3xxRedirection()).isTrue();
    assertThat(response.getHeaders().getLocation()).isNotNull();
  }
}
