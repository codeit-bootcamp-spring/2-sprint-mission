package com.sprint.mission.discodeit.storage.s3;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;

public class AWSS3Test {

    private final S3Client s3Client;
    private final S3Presigner presigner;

    public AWSS3Test() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            props.load(fis);

            String accessKey = props.getProperty("AWS_ACCESS_KEY");
            String secretKey = props.getProperty("AWS_SECRET_KEY");
            String region = props.getProperty("AWS_REGION", "ap-northeast-2");

            AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

            this.s3Client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();

            this.presigner = S3Presigner.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(".env 파일 로드 실패", e);
        }
    }

    // 파일 업로드
    public void uploadFile(String bucketName, String keyName, String filePath) {
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(keyName)
                            .build(),
                    RequestBody.fromFile(new File(filePath))
            );
            System.out.println("업로드 성공: " + keyName);
        } catch (Exception e) {
            System.err.println("업로드 실패: " + e.getMessage());
        }
    }

    // 파일 다운로드
    public void downloadFile(String bucketName, String keyName, String downloadPath) {
        try {
            s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(keyName)
                            .build(),
                    Paths.get(downloadPath)
            );
            System.out.println("다운로드 성공: " + downloadPath);
        } catch (Exception e) {
            System.err.println("다운로드 실패: " + e.getMessage());
        }
    }

    // Presigned URL 생성
    public URL generatePresignedUrl(String bucketName, String keyName, Duration duration) {
        try {
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
                    GetObjectPresignRequest.builder()
                            .signatureDuration(duration)
                            .getObjectRequest(GetObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(keyName)
                                    .build())
                            .build()
            );
            return presignedRequest.url();
        } catch (Exception e) {
            System.err.println("Presigned URL 생성 실패: " + e.getMessage());
            return null;
        }
    }

    // 테스트 메소드
    public void testUpload() {
        uploadFile("discodeit-binary-content-storage-bk", "test-file.txt", "./test-upload.txt");
    }

    public void testDownload() {
        downloadFile("discodeit-binary-content-storage-bk", "test-file.txt", "./downloaded-file.txt");
    }

    public void testPresignedUrl() {
        URL url = generatePresignedUrl(
                "discodeit-binary-content-storage-bk",
                "test-file.txt",
                Duration.ofMinutes(10)
        );
        if (url != null) {
            System.out.println("Presigned URL: " + url);
        }
    }

    public static void main(String[] args) {
        AWSS3Test tester = new AWSS3Test();
        tester.testUpload();
        tester.testDownload();
        tester.testPresignedUrl();
    }
}
