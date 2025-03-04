package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Channel extends Common {

    // 해당 채널의 주인
    private final User owner;
    // 채널명
    // 채널명을 바꿀 수 있게 하고싶었으나, id값은 UUID 타입이라 DB가 없는 지금은 마음대로 조회하기 힘들 것 같아서
    // channelName을 바꿀 수 없게 만들고 중복 불가능하게 만들어서 id값 대신 사용할 예정 (괜찮을까요?)
    private final String channelName;

    // 해당 채널의 유저리스트
    // 중복이어도 예외처리가 필요할 정도로 중요하지 않음 -> 중복만 방지되게끔 Set 사용
    private Set<User> users = new HashSet<>();

    // 해당 채널이 가진 메시지 목록
    private List<Message> messages = new ArrayList<>();

    public Channel(User user, String channelName) {
        // super() 하지 않아도 기본 생성자는 컴파일러가 자동으로 삽입
        this.owner = user;
        this.channelName = channelName;
        users.add(user);
    }

    public User getOwner() {return owner; }

    public String getChannelName() {
        return channelName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    // 유저 추가 - 유저가 채널에 들어올 경우
    public void addUsers(User user) {
        users.add(user);
    }

    // 유저 삭제 - 유저가 채널에 나갈 경우
    public void removeUsers(User user) {
        users.remove(user);
    }

    // 메시지 추가
    public void addMessage(Message message) {
        messages.add(message);
    }

    // 메시지 삭제
    public void removeMessage(Message message) {
        messages.remove(message);
    }


    @Override
    public String toString() {
        return "Channel{" +
                "user=" + owner +
                ", channelName='" + channelName + '\'' +
                ", users=" + users +
                '}';
    }
}
