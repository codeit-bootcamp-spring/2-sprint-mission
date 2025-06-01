package com.sprint.mission.discodeit.stoarge.s3;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

// S3 테스트는 실제 비용이 발생할 수 있으므로, CI 환경 등에서는 실행되지 않도록 주의해야 합니다.
// @EnabledIfSystemProperty(named = "runS3Tests", matches = "true") // 필요시 활성화
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 테스트 순서 지정을 위해 추가
public class AWSS3Test {

    private static Properties awsProperties;
    private static String awsAccessKeyId;
    private static String awsSecretAccessKey;
    private static String awsRegionValue;
    private static String s3BucketName;

    private static S3Client s3Client;
    private static S3Presigner s3Presigner;

    // 테스트 간 공유될 파일 키
    private static String testFileKeyForDownload;
    private static String testFileContent = "Hello S3 from AWSS3Test for download and presigned URL!";

    @BeforeAll
    static void setUp() throws IOException { // 초기화 실패 시 테스트 전체 스킵을 위해 IOException throw
        awsProperties = new Properties();
        Path envFilePath = Paths.get(".env").toAbsolutePath(); // 프로젝트 루트의 .env 가정

        if (!Files.exists(envFilePath)) {
            System.err.println(".env file not found at: " + envFilePath);
            System.err.println("Skipping S3 tests. Please ensure .env file exists at project root and contains AWS S3 test properties.");
            assumeTrue(false, "Could not find .env file for S3 tests.");
            return; // .env 파일 없으면 여기서 중단
        }

        try (InputStream input = new FileInputStream(envFilePath.toFile())) {
            awsProperties.load(input);
            awsAccessKeyId = awsProperties.getProperty("AWS_ACCESS_KEY_ID");
            awsSecretAccessKey = awsProperties.getProperty("AWS_SECRET_ACCESS_KEY");
            awsRegionValue = awsProperties.getProperty("AWS_REGION");
            s3BucketName = awsProperties.getProperty("AWS_S3_BUCKET_NAME");

            assumeTrue(awsAccessKeyId != null && !awsAccessKeyId.isBlank(), "AWS_ACCESS_KEY_ID not set in .env");
            assumeTrue(awsSecretAccessKey != null && !awsSecretAccessKey.isBlank(), "AWS_SECRET_ACCESS_KEY not set in .env");
            assumeTrue(awsRegionValue != null && !awsRegionValue.isBlank(), "AWS_REGION not set in .env");
            assumeTrue(s3BucketName != null && !s3BucketName.isBlank(), "AWS_S3_BUCKET_NAME not set in .env");

            System.out.println("AWS Properties loaded for S3 tests. Bucket: " + s3BucketName + ", Region: " + awsRegionValue);

            Region region = Region.of(awsRegionValue);
            AwsBasicCredentials credentials = AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey);
            StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

            s3Client = S3Client.builder()
                    .region(region)
                    .credentialsProvider(credentialsProvider)
                    .build();

            s3Presigner = S3Presigner.builder()
                    .region(region)
                    .credentialsProvider(credentialsProvider)
                    .build();

            // 다운로드 및 Presigned URL 테스트를 위한 공통 파일 업로드
            testFileKeyForDownload = "test-files/s3-common-test-file-" + UUID.randomUUID() + ".txt";
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(s3BucketName)
                            .key(testFileKeyForDownload)
                            .contentType("text/plain")
                            .build(),
                    RequestBody.fromString(testFileContent));
            System.out.println("Uploaded common test file to S3 with key: " + testFileKeyForDownload);


        } catch (IOException ex) {
            System.err.println("Error loading .env file or initializing S3 client: " + ex.getMessage());
            assumeTrue(false, "Could not initialize S3 client due to .env loading error or other issue.");
        }
    }

    @Test
    @Order(1) // 업로드 테스트를 먼저 실행
    @Disabled // 실제 구현 완료 후 주석 해제
    void testUploadToS3() {
        String fileKey = "test-files/s3-upload-specific-" + UUID.randomUUID() + ".txt";
        String content = "This is a specific upload test!";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3BucketName)
                .key(fileKey)
                .contentType("text/plain")
                .build();

        assertDoesNotThrow(() -> s3Client.putObject(putObjectRequest, RequestBody.fromString(content)),
                "S3 putObject should not throw an exception.");
        System.out.println("File uploaded to S3 with key: " + fileKey);

        // 업로드 확인 (HeadObject)
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder().bucket(s3BucketName).key(fileKey).build();
        assertDoesNotThrow(() -> s3Client.headObject(headObjectRequest), "File should exist in S3 after upload.");

        // 테스트 후 생성된 파일 삭제 (선택 사항)
        // deleteTestObject(fileKey);
    }

    @Test
    @Order(2) // 다운로드 테스트는 공통 파일 사용
    @Disabled // 실제 구현 완료 후 주석 해제
    void testDownloadFromS3() {
        assumeTrue(testFileKeyForDownload != null, "Common test file was not uploaded in BeforeAll.");

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3BucketName)
                .key(testFileKeyForDownload)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = assertDoesNotThrow(
                () -> s3Client.getObjectAsBytes(getObjectRequest),
                "S3 getObjectAsBytes should not throw an exception."
        );

        String downloadedContent = objectBytes.asUtf8String();
        System.out.println("Downloaded content: " + downloadedContent);

        assertEquals(testFileContent, downloadedContent, "Downloaded content should match expected content.");
    }

    @Test
    @Order(3) // Presigned URL 테스트도 공통 파일 사용
    @Disabled // 실제 구현 완료 후 주석 해제
    void testGeneratePresignedUrl() {
        assumeTrue(testFileKeyForDownload != null, "Common test file was not uploaded in BeforeAll.");

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
               .bucket(s3BucketName)
               .key(testFileKeyForDownload)
               .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
               .signatureDuration(Duration.ofMinutes(15)) // URL 유효 시간 15분
               .getObjectRequest(getObjectRequest)
               .build();

        PresignedGetObjectRequest presignedRequest = assertDoesNotThrow(
                () -> s3Presigner.presignGetObject(getObjectPresignRequest),
                "S3 presignGetObject should not throw an exception."
        );

        String presignedUrl = presignedRequest.url().toString();
        System.out.println("Generated Presigned URL: " + presignedUrl);

        assertNotNull(presignedUrl, "Presigned URL should not be null.");
        assertTrue(presignedUrl.startsWith("https://"+s3BucketName+".s3."+awsRegionValue+".amazonaws.com/"+testFileKeyForDownload),
                "Presigned URL format is not as expected. URL: " + presignedUrl);
        // 추가: 실제로 생성된 URL로 HTTP GET 요청을 보내 파일 내용을 확인하는 것도 좋은 검증 방법입니다.
        // (단, 이 테스트는 네트워크 연결 및 외부 HTTP 클라이언트 라이브러리가 필요할 수 있습니다.)
    }
    
    @AfterAll
    static void tearDown() {
        // @BeforeAll에서 생성한 공통 테스트 파일 삭제
        if (s3Client != null && testFileKeyForDownload != null) {
            deleteTestObject(testFileKeyForDownload);
            System.out.println("Deleted common test file from S3: " + testFileKeyForDownload);
        }
        // S3 클라이언트 및 프리사이너 닫기
        if (s3Client != null) {
            s3Client.close();
        }
        if (s3Presigner != null) {
            s3Presigner.close();
        }
    }

    // 테스트 객체 삭제 헬퍼
    private static void deleteTestObject(String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(s3BucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            System.err.println("Error deleting test object " + key + " from S3: " + e.getMessage());
        }
    }
} 