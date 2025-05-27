package com.sprint.mission.discodeit.storage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.s3.model.S3Exception;

@SpringBootTest
@ActiveProfiles("test")
class S3BinaryContentStorageTest {

  @Autowired
  private BinaryContentStorage storage;

  private UUID id;
  private byte[] content;
  private BinaryContentDto binaryContentDto;

  @BeforeEach
  void setUp() {
    id = UUID.randomUUID();
    content = "hello".getBytes();
    binaryContentDto = new BinaryContentDto(id, "s3test.txt", (long) content.length, "text/plain");
  }

  // cf. ApplicationContextInitializer 사용
  @BeforeAll
  static void loadEnv() throws IOException {
    Properties props = new Properties();
    props.load(Files.newBufferedReader(Paths.get(".env")));

    props.forEach((key, value) -> {
      System.setProperty((String) key, (String) value);
    });
  }

  @Test
  void testPutSuccess() {
    UUID resultId = storage.put(id, content);
    assertEquals(id, resultId);
  }

  @Test
  void testGetSuccess() throws IOException {
    storage.put(id, content);

    InputStream inputStream = storage.get(id);
    byte[] downloaded = inputStream.readAllBytes();

    assertArrayEquals(content, downloaded);
  }

  @Test
  void testDownload() {
    storage.put(id, content);

    ResponseEntity<?> response = storage.download(binaryContentDto);

    assertEquals(HttpStatus.FOUND, response.getStatusCode());
    assertTrue(response.getHeaders().containsKey("Location"));
    assertTrue(response.getHeaders().getFirst("Location").startsWith("https://"));
  }

  @Test
  void testDelete() {
    storage.put(id, content);
    storage.deleteById(id);

    assertThrows(S3Exception.class, () -> storage.get(id));
  }
}
