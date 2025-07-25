package com.sprint.mission.discodeit.testutil;

import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;


@ContextConfiguration(classes = TestContainerSupport.TestContainersConfiguration.class)
abstract class TestContainerSupport {

  private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>(
      DockerImageName.parse("postgres:16-alpine"))
      .withDatabaseName("testdb")
      .withUsername("testuser")
      .withPassword("testpass")
      .withTmpFs(Map.of("/var/lib/postgresql/data", "rw"));

  private static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer(
      DockerImageName.parse("localstack/localstack:3.5.0"))
      .withServices(
          LocalStackContainer.Service.S3
      );

  static {
    POSTGRES_CONTAINER.start();
    LOCAL_STACK_CONTAINER.start();
  }

  @DynamicPropertySource
  static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);

    registry.add("discodeit.storage.s3.access-key", LOCAL_STACK_CONTAINER::getAccessKey);
    registry.add("discodeit.storage.s3.secret-key", LOCAL_STACK_CONTAINER::getSecretKey);
    registry.add("discodeit.storage.s3.region", LOCAL_STACK_CONTAINER::getRegion);
  }

  @TestConfiguration
  static class TestContainersConfiguration {

    private static final StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(
            LOCAL_STACK_CONTAINER.getAccessKey(),
            LOCAL_STACK_CONTAINER.getSecretKey()
        )
    );
    private static final Region region = Region.of(LOCAL_STACK_CONTAINER.getRegion());
    private static final URI endpointURI = LOCAL_STACK_CONTAINER.getEndpointOverride(Service.S3);

    @Bean
    public S3Client s3Client() {
      return S3Client.builder()
          .endpointOverride(endpointURI)
          .region(region)
          .credentialsProvider(credentialsProvider)
          .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
      return S3Presigner.builder()
          .endpointOverride(endpointURI)
          .region(region)
          .credentialsProvider(credentialsProvider)
          .build();
    }

    /**
     * 초기 버킷 생성
     */
    @Bean
    public CommandLineRunner createInitialBucket(
        S3Client s3Client,
        @Value("${discodeit.storage.s3.bucket}") String bucket
    ) {
      return args -> s3Client.createBucket(builder -> builder.bucket(bucket));
    }
  }

}
