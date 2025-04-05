package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;


public class ReadStatusDto {

  @Getter
  @Setter
  @Builder(toBuilder = true)
  @AllArgsConstructor
  public static class Create {

    @NotNull
    private UUID channelId;
    @NotNull
    private UUID userId;
  }

  @Builder
  @Getter
  @Setter
  public static class Update {

    @NotNull
    private UUID lastReadMessageId;
  }


  @Getter
  @Setter
  @NotBlank
  public static class ResponseReadStatus {

    @NotNull
    private UUID id;
    @NotNull
    private UUID channelId;
    @NotNull
    private UUID userId;
    @NotNull
    private UUID lastReadMessageId;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
  }
} 