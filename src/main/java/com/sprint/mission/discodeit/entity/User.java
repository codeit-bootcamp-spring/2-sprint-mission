package com.sprint.mission.discodeit.entity;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User extends Common{
    // 아이디 - 디스코드에서는 이메일 사용
    private final String username;
    // 비밀번호
    private String password;
    // 닉네임
    private String nickname;
    // 권한 - 채널 여러개의 권한이 필요하므로 List로 생성
    // 채널마다 관리자 권한명을 다르게 해줘야함 -> enum보단 String 사용?
    private List<String> roles = new ArrayList<>();

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

    public List<String> getRoles() {
        return roles;
    }

    // username은 아이디 변경이 불가능하므로 update 필요 X

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 권한 추가
    public void addRole(String role) {
        roles.add(role);
    }

    //권한 삭제
    public void removeRole(String role) {
        roles.remove(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", roles=" + roles +
                '}';
    }
}
