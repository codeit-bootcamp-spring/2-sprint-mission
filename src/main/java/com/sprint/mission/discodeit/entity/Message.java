package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne()
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne()
    @JoinColumn(name = "author_id")
    private User author;
    
    @ManyToMany()
    @JoinTable(
            name = "message_attachments",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private List<BinaryContent> attachments;

    protected Message() {

    }

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
