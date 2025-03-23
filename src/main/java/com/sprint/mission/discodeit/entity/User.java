package com.sprint.mission.discodeit.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID profileId;
    private String username;
    private String password;
    private String email;

    public User(UUID id, Instant createdAt, String username, String password, String email, UUID profileId) {
        super(id, createdAt);
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileId = profileId;
    }

    public void userUpdate(Instant updateAt, String username, String email){
        setUpdatedAt(updateAt);
        this.username = username;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                super.toString() +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}