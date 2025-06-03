package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.config.S3StorageProperties;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class S3BinaryContentStorageTest {

    private S3BinaryContentStorage storage;

    @BeforeEach
    void init() {
        // AWSS3Env  유틸 그대로 사용
        S3StorageProperties props = new S3StorageProperties();
        props.setAccessKey(AWSS3Env.accessKey());
        props.setSecretKey(AWSS3Env.secretKey());
        props.setRegion(AWSS3Env.region());
        props.setBucket(AWSS3Env.bucket());

        storage = new S3BinaryContentStorage(props);
    }

    @Test
    void put_and_download_redirect() {
        UUID id = UUID.randomUUID();
        byte[] bytes = "Hello".getBytes();
        storage.put(id, bytes);

        BinaryContentDto dto = new BinaryContentDto(
            id,
            "hello.txt",
            (long) bytes.length,      // size
            "text/plain"              // contentType
        );

        var resp = storage.download(dto);
        assertThat(resp.getStatusCode().value()).isEqualTo(302);
        assertThat(resp.getHeaders().getFirst(HttpHeaders.LOCATION))
            .startsWith("https://");
    }
}
