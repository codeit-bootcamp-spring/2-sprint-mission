package com.sprint.mission.discodeit.entity;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.*;

public class User extends Common implements Serializable {

    private static final long serialVersionUID = 1L;

    // 아이디 - 디스코드에서는 이메일 사용
    private final String username;
    // 비밀번호
    private String password;
    // 닉네임
    private String nickname;

    // 유저가 들어가있는 채널 목록 - 유저가 디스코드에 접속했을 때, 본인의 채널 목록을 보여줘야하므로 필요
    // 중복 예외 처리가 따로 필요하지 않고, 중복만 방지하면서 순서 유지가 필요 -> LinkedHashSet 사용
    private Set<Channel> channels = new LinkedHashSet<>();

    // 유저가 남긴 메시지 목록 - 유저가 어떤 채널에 어떤 메시지를 남겼는지 관리 목적에서 필요
    // 중복이 있어도 됨 -> List 사용
    private List<Message> messages = new ArrayList<>();

    // 권한 - 채널 여러개의 권한이 필요하므로 List로 생성
    // 중복을 허용하면 안되니까 Set이 더 나으려나요? vs List로 하고 중복일 경우 예외처리
    // => 로직상으로 중복돼도 별다른 문제가 없고, 단순히 중복 자체만 방지하면 되니까 Set 사용?
    // <-> 중복 회원 가입은 로직상으로 반드시 막아야 하므로 List로 하고 예외처리까지 해주기
    private Set<String> roles = new HashSet<>();

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

    public Set<String> getRoles() {
        return roles;
    }

    public Set<Channel> getChannels() {
        return channels;
    }

    public List<Message> getMessages() {
        return messages;
    }

    // username은 아이디 변경이 불가능하므로 update 필요 X

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 채널 추가
    public void addChannel(Channel channel) {
        channels.add(channel);
    }

    // 채널 삭제
    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    // 메시지 추가
    public void addMessage(Message message) {
        messages.add(message);
    }

    // 메시지 삭제
    public void deleteMessage(Message message) {
        messages.remove(message);
    }

    // 권한 추가
    public void addRole(String role) {
        roles.add(role);
    }

    // 권한 삭제
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
