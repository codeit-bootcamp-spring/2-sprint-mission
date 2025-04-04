package com.sprint.mission.discodeit.util.mock.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public final class MockFile {

  private MockFile() {
  }

  public static MultipartFile createMockImageFile(String fileName) {
    return new MockMultipartFile(
        fileName,
        null,
        MediaType.IMAGE_JPEG_VALUE,
        loadImageFileFromResource(fileName));
  }

  private static byte[] loadImageFileFromResource(String fileName) {
    byte[] binaryProfileImage;
    try (InputStream inputStream = MockFile.class.getClassLoader().getResourceAsStream(fileName)) {
      assert inputStream != null;
      binaryProfileImage = inputStream.readAllBytes();
    } catch (IOException e) {
      throw new UncheckedIOException("src/resources 파일에" + fileName + "파일이 없습니다.", e);
    }

    return binaryProfileImage;
  }
}
