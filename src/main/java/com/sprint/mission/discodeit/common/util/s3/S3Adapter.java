package com.sprint.mission.discodeit.common.util.s3;

import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public interface S3Adapter {

  CompletableFuture<PutObjectResponse> put(PutObjectRequest putRequest, RequestBody requestBody);

  InputStream get(GetObjectRequest getRequest);

  URI createDownloadUrl(GetObjectRequest getObjectRequest, long expirationSeconds);

}
