package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.exception.s3.S3DeleteException;
import com.sprint.mission.discodeit.exception.s3.S3DownloadException;
import com.sprint.mission.discodeit.exception.s3.S3UploadException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Slf4j
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;

  @Override
  public void put(UUID binaryContentId, byte[] bytes) {
    String key = binaryContentId.toString();
    Tika tika = new Tika();

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType(tika.detect(bytes))
        .contentLength((long) bytes.length)
        .build();
    try {
      s3Client.putObject(
          putObjectRequest,
          RequestBody.fromBytes(bytes));
    } catch (S3Exception e) {
      log.error("S3 upload failed (key: {})", key);
      throw new S3UploadException(Map.of("key", key));
    }
  }


  @Override
  public InputStream get(UUID binaryContentId) {
    String key = binaryContentId.toString();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();
    try {
      ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
      return s3Object;
    } catch (S3Exception e) {
      log.error("S3 download failed (key: {})", key);
      throw new S3DownloadException(Map.of("key", key));
    }
  }

  @Override
  public void delete(UUID binaryContentId) {
    String key = binaryContentId.toString();

    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    try {
      s3Client.deleteObject(deleteObjectRequest);
    } catch (S3Exception e) {
      log.error("S3 delete failed (key: {})", key);
      throw new S3DeleteException(Map.of("key", key));
    }
  }

  @Override
  public ResponseEntity<?> download(FindBinaryContentResult findBinaryContentResult) {
    String key = findBinaryContentResult.id().toString();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    // Presigned URL을 만들기 위한 요청 객체 생성
    PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(
        GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .getObjectRequest(getObjectRequest) //  실제 S3 객체 다운로드 요청 정보를 포함
            .build());

    String presignedUrl = presignedGetObjectRequest.url().toString();

    // 302/FOUND 응답 반환 + Location 헤더에 Presigned URL을 담아 리다이렉트 (URL로 자동이동)
    return ResponseEntity.status(HttpStatus.FOUND)
        .header(HttpHeaders.LOCATION, presignedUrl)
        .build();
  }
}
