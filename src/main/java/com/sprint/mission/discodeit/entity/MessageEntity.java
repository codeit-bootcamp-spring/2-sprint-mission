package com.sprint.mission.discodeit.entity;

import java.util.*;

public class MessageEntity extends BaseEntity {
    private String content;
    private UserEntity sender;
    private ChannelEntity channel;
    private Map<String, Integer> reactions;


    public MessageEntity(String content, UserEntity sender, ChannelEntity channel) {
        super();
        this.content = content;
        this.sender = sender;
        this.channel = channel;
        this.reactions = new HashMap<>();
    }

    public String getContent() { return content; }
    public UserEntity getSender() { return sender; }
    public ChannelEntity getChannel() { return channel;}
    public Map<String, Integer> getReactions() { return reactions; }

        public long getCreatedTime(){
            return getCreatedAt();
        }

    public void updateMessage(String newContent) {
        this.content = newContent;
        updateUpdatedAt();
    }

    public void deleteMessage(List<MessageEntity> messages) {
        messages.remove(this);
    }

    public void addReaction(String emoji){
        reactions.put(emoji, reactions.getOrDefault(emoji, 0) + 1);
    }

    public void removeReaction(String emoji){
        if(reactions.containsKey(emoji)){
            int count = reactions.get(emoji);
            if (count > 1){
                reactions.put(emoji, count - 1);
            } else {
                reactions.remove(emoji);
            }
        }
    }
}


