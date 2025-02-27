package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String userName;

    public User(String userName) {
        super(); //id, createdAt, updatedAt
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        // 데이터 필드를 조작하기 위한 기본 규칙 (예: 10살은 가입 못한다.)
        // -> 업데이트 방향 getter setter 캡슐화
        this.userName = userName;
        this.updatedAt = System.currentTimeMillis();
    }

    //유저 객체 생성하면 toString() 메소드 자동 반영돼서 출력함.
    @Override
    public String toString() {
        return "{" +
                "userName='" + userName + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
