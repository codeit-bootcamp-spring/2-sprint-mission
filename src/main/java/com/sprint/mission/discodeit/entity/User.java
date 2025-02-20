package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String userName;

    public User(String userName) {
        super();
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "사용자 정보 : {" +
                "userName='" + userName + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
