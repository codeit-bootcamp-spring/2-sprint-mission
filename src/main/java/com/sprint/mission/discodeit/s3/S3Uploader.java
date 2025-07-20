package com.sprint.mission.discodeit.s3;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Profile("!local")
@Component
@Slf4j
@RequiredArgsConstructor
public class S3Uploader {

    private static final String BUCKET_DOMAIN = "버킷의 객체들이 갖는 도메인 url";
    private final S3Presigner s3Presigner;
    @Value("${AWS_S3_BUCKET}")
    private String bucket;


    public Map<String, String> createPresignedUrl(String imageExtension) {

        String keyName = UUID.randomUUID() + "." + imageExtension;
        keyName = keyName.replace("-", "");
        String contentType = "image/" + imageExtension;
        Map<String, String> metadata = Map.of("fileType", contentType, "Content-Type", contentType);

        PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucket).key(keyName)
            .metadata(metadata).build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10)).putObjectRequest(objectRequest).build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        String myURL = presignedRequest.url().toString();
        myURL = myURL.replace(BUCKET_DOMAIN, "");
        String publicUrl = BUCKET_DOMAIN + keyName;
        log.info("HTTP method: {}", presignedRequest.httpRequest().method());

        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("uploadUrl", myURL);
        map.put("publicUrl", publicUrl);

        return map;
    }

}


