package com.sprint.mission.discodeit.binarycontent.storage;

import com.sprint.mission.discodeit.IntegrationTestSupport;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Disabled
public class S3BinaryContentStorageTest extends IntegrationTestSupport {

    @Autowired
    private BinaryContentStorage binaryContentStorage;

    @Value("${discodeit.storage.s3.region}")
    private String region;

    @DisplayName("UUID와 바이트 배열을 전달하면 S3에 저장된다.")
    @Test
    void put() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        byte[] data = "test-data".getBytes(StandardCharsets.UTF_8);

        // when
        UUID resultId = binaryContentStorage.put(id, data);

        // then
        InputStream inputStream = binaryContentStorage.get(resultId);
        byte[] fetched = inputStream.readAllBytes();
        Assertions.assertThat(new String(fetched)).isEqualTo("test-data");
    }

    @DisplayName("UUID를 이용하여 S3에 저장된 파일을 읽을 수 있다.")
    @Test
    void get() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        byte[] data = "another-test".getBytes(StandardCharsets.UTF_8);
        binaryContentStorage.put(id, data);

        // when
        InputStream result = binaryContentStorage.get(id);

        // then
        byte[] read = result.readAllBytes();
        Assertions.assertThat(new String(read)).isEqualTo("another-test");
    }

    @DisplayName("UUID를 이용하여 Presigned URL을 통해 파일 다운로드 경로를 얻을 수 있다.")
    @Test
    void download() {
        // given
        UUID id = UUID.randomUUID();
        binaryContentStorage.put(id, "url-test".getBytes(StandardCharsets.UTF_8));

        // when
        ResponseEntity<?> response = binaryContentStorage.download(id);
        RestTemplate restTemplate = new RestTemplate();
        response.getHeaders().getLocation();
        byte[] downloadedBytes = restTemplate.getForObject(response.getHeaders().getLocation(), byte[].class);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getStatusCode().is3xxRedirection()).isTrue();
            softly.assertThat(new String(downloadedBytes)).isEqualTo("url-test");
        });
    }

}
