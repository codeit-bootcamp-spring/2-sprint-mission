package com.sprint.mission.discodeit.entity;

import java.util.HashSet;
import java.util.Set;

public class Channel extends BaseEntity {
    private String channelName;
    private Set<String> members;
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

    public Set<String> getMembers() {
        return members;
    }

    public void updateMembers(String newMember) {
        this.members.add(newMember);
    }

    public void removeMember(String member) {
        this.members.remove(member);
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void updateMessages(Message newMessages) {
        this.messages.add(newMessages);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
    }

    public boolean isUserInChannel(String userName){
        return this.members.contains(userName);
    }

    public boolean isMessageInChannel(Message message){
        return this.members.contains(message);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelname ='" + channelName + '\'' +
                ", members= " + members +
                '}';
    }
}
