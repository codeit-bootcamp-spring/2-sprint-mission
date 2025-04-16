package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class Message extends BaseUpdatableEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String content;

    private Channel channel;
    private User author;
    private List<BinaryContent> attachments;

    public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
        this.content = content;
        this.author = author;
        this.channel = channel;
        this.attachments = (attachments != null) ? attachments : List.of();
    }

    public void updateContent(String newContent) {
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
        }
    }
}
