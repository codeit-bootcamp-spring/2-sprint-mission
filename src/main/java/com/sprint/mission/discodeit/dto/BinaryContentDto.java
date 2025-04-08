package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.dto.common.CompositeIdentifier;
import com.sprint.mission.discodeit.dto.common.FileMetadata;
import com.sprint.mission.discodeit.dto.common.TimeStamps;
import com.sprint.mission.discodeit.dto.common.UserData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class BinaryContentDto {


  @Getter
  @Builder(toBuilder = true)
  public static class Upload { // 메타데이터 전용 DTO

    @NotNull()
    private final UUID ownerId;
    @NotBlank()
    private final String ownerType;
    @NotBlank
    private final MultipartFile file;
    private final String fileName;
    private final String contentType;
  }

  @Getter
  @Builder(toBuilder = true)
  public static class Summary {

    private FileMetadata fileMetadata;
    private CompositeIdentifier compositeIdentifier;
    private ZonedDateTime createdAt;
  }

  @Getter
  @Builder(toBuilder = true)
  public static class DeleteResponse {

    private FileMetadata fileMetadata;
    private CompositeIdentifier compositeIdentifier;
    @NotNull
    private UUID id;
    @NotBlank
    private String fileName;
    @NotBlank
    private boolean success;
    private String message;
  }
}

