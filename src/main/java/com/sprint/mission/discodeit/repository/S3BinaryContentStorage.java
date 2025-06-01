package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.file.FileNotFoundCustomException;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
@Repository
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final BinaryContentRepository binaryContentRepository;
    private final String bucketName;
    private final Long presignedUrlExpirationSeconds;

    public S3BinaryContentStorage(S3Client s3Client, S3Presigner s3Presigner,
        BinaryContentRepository binaryContentRepository,
        @Qualifier("s3BucketName") String bucketName,
        @Qualifier("s3PresignedUrlExpirationSeconds") Long presignedUrlExpirationSeconds) { // 여기에도!
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.binaryContentRepository = binaryContentRepository;
        this.bucketName = bucketName;
        this.presignedUrlExpirationSeconds = presignedUrlExpirationSeconds;
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        BinaryContent binaryContent = validateFileExists(id);
        // id+ contentType 으로  키설정
        String s3Key = binaryContent.generateS3Key();
        log.info("S3에 파일 업로드 시도");

        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(s3Key)
            .contentType(binaryContent.getContentType()).build();

        try {

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
            log.info("S3에 파일 업로드 성공: 키 [{}]", s3Key);
            return id;
        } catch (FileProcessingCustomException e) {
            throw new FileProcessingCustomException(Map.of("id", id, "message", e.getMessage()));
        }
    }

    @Override
    public InputStream get(UUID id) {
        BinaryContent binaryContent = validateFileExists(id);
        String s3Key = binaryContent.generateS3Key();
        if (s3Key == null) {
            throw new FileProcessingCustomException(Map.of("id", id));
        }
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(s3Key)
            .build();
        try {
            return s3Client.getObject(getObjectRequest);
        } catch (S3Exception e) {
            log.error("S3 파일 가져오는데 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Void> download(UUID id) {
        BinaryContent binaryContent = validateFileExists(id);
        String s3Key = binaryContent.generateS3Key();

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName)
                .key(s3Key).build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(presignedUrlExpirationSeconds))
                .getObjectRequest(getObjectRequest).build();
            URL url = s3Presigner.presignGetObject(presignRequest).url();
            URI locationUri = url.toURI();

            return ResponseEntity.status(HttpStatus.FOUND).location(locationUri).build();
        } catch (FileNotFoundCustomException e) {
            log.warn("다운로드 실패 (파일 없음)");
            throw e;
        } catch (FileProcessingCustomException e) {
            log.error("다운로드 실패 (파일 처리 오류)");
            throw e;
        } catch (Exception e) {
            log.error("다운로드 실패 (예상치 못한 오류)");
            throw new FileProcessingCustomException(
                Map.of("operation", "download-unexpected", "filePath", binaryContent.toString(),
                    "customMessageContext", "다운로드 중 예상치 못한 오류 발생: " + e.getMessage()));
        }


    }

    @Override
    public void delete(UUID id) {
        BinaryContent binaryContent = validateFileExists(id);
        String s3Key = binaryContent.generateS3Key();

        if (s3Key == null) {
            throw new FileProcessingCustomException(
                Map.of("id", id, "message", "S3 키를 생성할 수 없습니다."));
        }

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName)
            .key(s3Key).build();

        try {
            s3Client.deleteObject(deleteObjectRequest);
            log.info("S3에서 파일 삭제 성공: 키 [{}]", s3Key);
        } catch (S3Exception e) {
            log.error("S3 파일 삭제 실패: 키 [{}], 오류: {}", s3Key, e.getMessage());
            throw new FileProcessingCustomException(
                Map.of("operation", "delete-s3-file", "s3Key", s3Key, "customMessageContext",
                    "S3에서 파일 삭제 중 오류 발생: " + e.getMessage()));
        }
    }

    private BinaryContent validateFileExists(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow(
            () -> new FileNotFoundCustomException(
                Map.of("id", id, "message", "메타데이터를 찾을 수 없습니다.")));
        return binaryContent;
    }


}

