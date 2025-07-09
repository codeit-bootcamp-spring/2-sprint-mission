package com.sprint.mission.discodeit.common.s3;

import java.io.InputStream;
import java.net.URI;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public interface S3Manager {

  PutObjectResponse put(String key, String bucket, byte[] fileBytes, String fileType);

  InputStream get(String key, String bucket);

  URI createDownloadUrl(String key, String bucket, long expirationSeconds);

}
