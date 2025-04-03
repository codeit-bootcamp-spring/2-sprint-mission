package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User extends SharedEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    private String name;
    private String pwd;
    private String email;
    private UUID profileId;
    
    public User(String name, String pwd, String email, UUID profileId) {
        super();
        this.name = name;
        this.pwd = pwd;
        this.email = email;
        this.profileId = profileId;
    }

    public void updateName(String name) {
        this.name = name;
        setUpdatedAt(Instant.now());

    }
    public void updatePwd(String pwd) {
        this.pwd = pwd;
        setUpdatedAt(Instant.now());
    }

    public void updateEmail(String email) {
        this.email = email;
        setUpdatedAt(Instant.now());
    }

    public void updateProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    @Override
    public String toString() {
        return String.format("\n uuid: %s\n userPwd: %s\n userEmail: %s\n createdAt= %s\n updatedAt= %s\n",
                uuid, pwd, email, createdAt, updatedAt);
    }
}
