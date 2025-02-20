package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String nickname;
    private String email;

    public User(String nickname, String email) {
        super(); // BaseEntity 클래스의 생성자 호출 -> id, createdAt 초기화
        this.nickname = nickname;
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    // 업데이트 메서드 (닉네임과 이메일 변경)
    public void update(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
    }
}
