package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany()
    @JoinTable(name = "message_attachments",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id"))
    private List<BinaryContent> attachments;


    public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
        this.content = content;
        this.channel = channel;
        this.author = author;
        this.attachments = attachments;
    }

    public void updateMessage(String newMessage) {
        this.content = newMessage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), channel.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Message message)) return false;
        return getId() != null && channel != null &&
                getId().equals(message.getId()) &&
                channel.getId().equals(message.channel.getId());
    }
}
