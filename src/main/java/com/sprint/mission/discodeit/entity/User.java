package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User extends Common{
    // 아이디 - 디스코드에서는 이메일 사용
    private String username;
    // 비밀번호
    private String password;
    // 닉네임
    private String nickname;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    // username은 아이디 변경이 불가능하므로 update 필요 X

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
