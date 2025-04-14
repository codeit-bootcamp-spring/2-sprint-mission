package com.sprint.discodeit.sprint.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class users implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID profileId;
    private Instant createdAt;
    private Instant updatedAt;
    private String usersname;
    private String email;
    private String password;
    private UUID usersStatusId;
    private boolean deleted; // 삭제 여부 필드 추가

    @Builder
    public users(String usersname, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = null;
        this.usersname = usersname;
        this.email = email;
        this.password = password;
    }

    public void associateStatus(usersStatus usersStatus) {
        this.usersStatusId = usersStatus.getId();
    }

    public void associateProfileId(BinaryContent binaryContent) {
        this.profileId = binaryContent.getId();
    }

    public void update(String newusersname, String newEmail, String newPassword) {
        boolean anyValueUpdated = false;
        if (newusersname != null && !newusersname.equals(this.usersname)) {
            this.usersname = newusersname;
            anyValueUpdated = true;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
            anyValueUpdated = true;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    public void softDelete() {
        this.deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }


}
