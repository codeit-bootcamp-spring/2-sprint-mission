package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

//유저 dto
public class UserDto {

    @Getter
    @Builder(toBuilder = true)
    public static class Create {

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        private final String email;         // 이메일
        @NotBlank(message = "비밀번호는 필수입니다.")
        private final String password;      // 비밀번호
        private final MultipartFile profileImage;  // 선택적 프로필 이미지
    }
    @Getter
    @Builder(toBuilder = true)
    public static class FindByUser {
        @NotBlank(message="이메일을 입력하세요")
        @Email(message = "유효한 이메일 형식이어야 합니다")
        private final String email;
    }
    //응답 dto
    @Getter
    @Builder(toBuilder=true)
    public static class Response {
        @NotNull
        private final UUID id;//사용자 ID
        @NotBlank
        private final String email;           // 이메일//
        private final Set<UUID> belongChannels; // 가입한 채널 목록
        private final UUID profileImage;      // 프로필 이미지 ID
        private final ZonedDateTime createdAt; // 생성 시간
        private final ZonedDateTime updateAt;  // 업데이트 시간
        private final ZonedDateTime lastSeenAt; // 마지막 접속 시간

    }

    @Getter
    @Builder(toBuilder = true)
    public static class Summary {
        @NotNull  // null 체크가 필요
        private final UUID id;  // 유저 아이디

        @NotBlank
        private final String email; // 이메일

        private final UUID profileImage;  // 프로필 이미지 ID

        private final boolean online; // 유저의 상태


        public static Summary from(User user, UserStatus status) {
            boolean isOnline = status != null && status.isOnline();
            return Summary.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .profileImage(user.getProfileId())
                    .online(isOnline)
                    .build();
        }
    }


    @Getter
    @Builder(toBuilder = true)
    public static class Update {
        @NotBlank
        private final UUID id;
        private final String password;       // 변경(비밀번호)
        private final UUID profileImage;  // 변경(프로필)
    }

    @Getter
    @Builder(toBuilder = true)
    public static class AuthRequest { //인증
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        private final String email;         // 이메일
        @NotBlank(message = "비밀번호는 필수입니다.")
        private final String password;    // 비밀번호
    }
    //권한 부여
    @Getter
    @Builder(toBuilder = true)
    public static class AuthToken {
        @NotBlank()
        private final String token; //토큰반환
    }

    @Getter
    @Builder(toBuilder = true)
    public static class Login {
        private String email;
        private String password;
        
        // 기본 생성자
        public Login() {}
        
        // 파라미터가 있는 생성자
        public Login(String email, String password) {
            this.email = email;
            this.password = password;
        }
        
        // getter/setter
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}