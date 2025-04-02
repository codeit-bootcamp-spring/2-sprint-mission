package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.dto.common.Profile;
import com.sprint.mission.discodeit.dto.common.TimeStamps;
import com.sprint.mission.discodeit.dto.common.UserChannels;
import com.sprint.mission.discodeit.dto.common.UserData;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.util.StatusOperation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

//유저 dto
public class UserDto {

  private Profile profile;
  private TimeStamps timeStamps;
  private UserChannels userChannels;
  private UserData userData;

  @Getter
  @Builder(toBuilder = true)
  public static class Create {

    @NotBlank
    @Email
    private final String email;         // 이메일
    @NotBlank
    private final String password;      // 비밀번호
  }

  //응답 dto
  @Getter
  @Builder(toBuilder = true)
  public static class Response {

    @NotNull
    private final UUID id;//사용자 ID
    @NotBlank
    private final String email;
    @NotNull// 이메일//
    private final Set<UUID> belongChannels; // 가입한 채널 목록
    private final UUID profileImage;      // 프로필 이미지 ID
    private final ZonedDateTime createdAt; // 생성 시간
    private final ZonedDateTime updateAt;  // 업데이트 시간


  }

  @Getter
  @Builder(toBuilder = true)
  public static class Summary {

    @NotNull
    private final UUID id;  // 유저 아이디
    @NotBlank
    private final String email; // 이메일
    @NotNull
    private final Set<UUID> belongChannels;
    @NotBlank
    private ZonedDateTime createdAt;
    @NotBlank
    private ZonedDateTime updatedAt;
    @NotBlank
    private StatusOperation status; // 유저의 상태
    private UUID profileImage;


  }

  @Getter
  @Builder(toBuilder = true)
  public static class Update {

    @NotNull
    private final UUID id;
    @NotBlank
    private final String password;
    private final UUID profileImage;  // 변경(프로필)
  }

  @Getter
  @Builder(toBuilder = true)
  public static class Login {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
  }

  @AllArgsConstructor
  @Getter
  @Builder(toBuilder = true)
  public static class DeleteResponse {

    @NotNull
    private final String id;
    @NotNull
    private String success;
  }
}