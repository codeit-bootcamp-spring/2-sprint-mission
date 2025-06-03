package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.config.EnvLoader;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

public class AWSS3Test {

    public static void main(String[] args) throws Exception {
        Properties props = EnvLoader.loadEnv(".env");
        String accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        String secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        String region = props.getProperty("AWS_S3_REGION");
        String bucketName = props.getProperty("AWS_S3_BUCKET");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        S3Client s3Client = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();

        S3Presigner s3Presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();

        // 3. 업로드 테스트
        String key = "test-img.png";
        String uploadFilePath = "upload/스크린.png";

        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build(),
            Paths.get(uploadFilePath)
        );
        System.out.println("Upload complete.");

        // 4. 다운로드 테스트 (다운로드 받아서 로컬에 저장)
        String downloadFilePath = "upload/test-img.txt";
        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(
            GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()
        );
        try (FileOutputStream fos = new FileOutputStream(downloadFilePath)) {
            s3Object.transferTo(fos);
        }
        System.out.println("Download complete.");

        // 5. Presigned URL 생성 테스트 (URL 출력)
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(1))
            .getObjectRequest(getObjectRequest)
            .build();

        String presignedUrl = s3Presigner.presignGetObject(presignRequest).url().toString();
        System.out.println("Presigned URL: " + presignedUrl);

        // 6. 리소스 종료
        s3Client.close();
        s3Presigner.close();
    }
}
