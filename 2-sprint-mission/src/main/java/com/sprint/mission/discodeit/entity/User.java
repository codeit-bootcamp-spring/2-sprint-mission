package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private final String email;
    private String password;
    private String nickname;
    private UserStatus status;
    private UserRole role;

    public User(String email, String password, String nickname, UserStatus status, UserRole role) {
        super();
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public UserStatus getStatus() {
        return status;
    }

    public UserRole getRole() {
        return role;
    }

    public void updatePassword(String password) {
        this.password = password;
        updateTimestamp();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
        updateTimestamp();
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
        updateTimestamp();
    }

    public void updateRole(UserRole role) {
        this.role = role;
        updateTimestamp();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", status=" + status +
                ", role=" + role +
                '}';
    }

}
