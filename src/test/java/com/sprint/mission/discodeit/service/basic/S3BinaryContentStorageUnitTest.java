package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentJPARepository;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.storage.S3BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.http.SdkHttpRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class S3BinaryContentStorageUnitTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    @Mock
    private BinaryContentJPARepository binaryContentJPARepository;

    @InjectMocks
    private S3BinaryContentStorage s3BinaryContentStorage;


    @Test
    @DisplayName("[S3BinaryContentStorage][put] AWS S3 업로드 테스트")
    public void testPut() {
        UUID binaryId = UUID.randomUUID();
        byte[] bytes = "test".getBytes();
        BinaryContent binaryContent = new BinaryContent("messi", (long) bytes.length, "jpg");
        ReflectionTestUtils.setField(binaryContent, "id", binaryId);

        BinaryContentResponseDto binaryContentResponse = new BinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType()
        );

        String S3Key = binaryContent.getId() + "-" + binaryContent.getFileName();
        String bucket = "test-bucket";
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(S3Key)
                .contentType(binaryContent.getContentType())
                .build();

        given(binaryContentJPARepository.findById(binaryId)).willReturn(Optional.of(binaryContent));
        given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).willReturn(PutObjectResponse.builder().build());

        UUID result = s3BinaryContentStorage.put(binaryId, bytes);

        then(binaryContentJPARepository).should().findById(binaryId);
        then(s3Client).should().putObject(any(PutObjectRequest.class), any(RequestBody.class));

        assertEquals(binaryContent.getId(), result);
    }

    @Test
    @DisplayName("[S3BinaryContentStorage][download] AWS S3 다운로드 테스트")
    public void testDownload() throws MalformedURLException {
        BinaryContentResponseDto binaryContentResponse = new BinaryContentResponseDto(
                UUID.randomUUID(),
                "ronaldo",
                12345L,
                "png"
        );
        PresignedGetObjectRequest PresignedRequest = mock(PresignedGetObjectRequest.class);
        SdkHttpRequest httpRequest = mock(SdkHttpRequest.class);

        given(PresignedRequest.url()).willReturn(URI.create("http://test-url.com").toURL());
        given(httpRequest.method()).willReturn(SdkHttpMethod.GET);

        given(PresignedRequest.httpRequest()).willReturn(httpRequest);
        given(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).willReturn(PresignedRequest);

        ResponseEntity<?> result = s3BinaryContentStorage.download(binaryContentResponse);

        then(s3Presigner).should().presignGetObject(any(GetObjectPresignRequest.class));

        assertEquals(HttpStatus.FOUND, result.getStatusCode());
        assertEquals(URI.create("http://test-url.com"), result.getHeaders().getLocation());


    }

}
