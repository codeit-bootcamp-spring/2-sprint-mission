package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import java.io.Serializable;

import com.sprint.mission.discodeit.dto.UpdateDefinition;
import lombok.Getter;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAtSeconds;
    private Instant updatedAtSeconds;
    private String username;
    private String email;
    private String password;
    private UUID profileId;

    public User(String username, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAtSeconds = Instant.now();
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileId = null;
    }

    public void updateProfileImage(UUID profileId) {
        this.profileId = profileId;
    }

    public void update(UpdateDefinition updateDefinition, UUID newProfileId) {
        boolean anyValueUpdated = false;

        if (updateDefinition.getUsername() != null && !updateDefinition.getUsername().equals(this.username)) {
            this.username = updateDefinition.getUsername();
            anyValueUpdated = true;
        }
        if (updateDefinition.getEmail()!= null && !updateDefinition.getEmail().equals(this.email)) {
            this.email = updateDefinition.getEmail();
            anyValueUpdated = true;
        }
        if (updateDefinition.getPassword() != null && !updateDefinition.getPassword().equals(this.password)) {
            this.password = updateDefinition.getPassword();
            anyValueUpdated = true;
        }

        if (newProfileId != null && !newProfileId.equals(this.profileId)){
            this.profileId = newProfileId;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updatedAtSeconds = Instant.now();
        }
    }
}
