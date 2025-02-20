package com.sprint.mission.discodeit.entity;
//사용자 정보를 관리
public class User extends BaseEntity {
    private String name;

    public User(String name) {
        super(); //부모 클래스 초기화
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
        update();
    }

}

