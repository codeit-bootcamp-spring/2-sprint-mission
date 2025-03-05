package com.sprint.mission.discodeit.entity;

// parent = BaseEntity
public class User extends BaseEntity {
    // 사용자 이름
    private String username;

    public User(String username){
        // BaseEntity를 통해 id할당 및 만들어진 시간 기록
        super();
        this.username = username;
    }

    public String getUsername() { return username; }
    public void updateUser(String username) {
        this.username = username;
        updateTimestamp();
    }
}
