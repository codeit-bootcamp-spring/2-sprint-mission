package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

//사용자 정보를 관리
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private final UUID id;

    public User(String username) {
        super(); //부모 클래스 초기화
        this.username = username;
        this.id = UUID.randomUUID();
    }

    public String getUserName() {
        return username;
    }

    public void updateUserName(String username) {
        this.username = username;
        update();
    }

}

