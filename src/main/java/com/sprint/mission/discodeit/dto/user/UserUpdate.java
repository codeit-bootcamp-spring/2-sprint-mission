package com.sprint.mission.discodeit.dto.user;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserUpdate {
    private UUID userID;
    private String newUserName;
    private String newEmail;
    private String newPassword;
    private UUID newProfileID;
}
