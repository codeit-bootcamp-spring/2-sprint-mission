package com.sprint.mission.discodeit.entity;

import java.util.HashSet;
import java.util.Set;

public class Channel extends BaseEntity {
    private String channelName;
    private Set<User> members;
    private Set<Message> messages;

    public Channel(String channelName) {
        super();
        this.channelName = channelName;
        this.members = new HashSet<>();
        this.messages = new HashSet<>();
    }

    public String getChannelName() {
        return channelName;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void addMembers(User newMember) {
        this.members.add(newMember);
    }

    public void removeMember(User member) {
        this.members.remove(member);
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void addMessages(Message newMessages) {
        this.messages.add(newMessages);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
    }

    public boolean isUserInChannel(User member){
        return this.members.contains(member);
    }

    public boolean isMessageInChannel(Message message){
        return this.messages.contains(message);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +  // 오타 수정
                ", members=" + members +
                '}';
    }
}
