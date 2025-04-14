package com.sprint.discodeit.sprint.domain.entity;

import com.sprint.discodeit.sprint.domain.base.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Users extends BaseUpdatableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID profileId;
    private String usersname;
    private String email;
    private String password;
    private UUID usersStatusId;
    private boolean deleted; // 삭제 여부 필드 추가


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

    }

    public void softDelete() {
        this.deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }


}
