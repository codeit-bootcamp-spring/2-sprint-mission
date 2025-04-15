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

    @OneToMany(cascade = CascadeType.ALL)
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
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Message message) {
            return message.getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "\nMessage ID: " + this.getId() +
                "\nchannelID: " + channel + "\nSenderID: " + author + "\nMessage: " + content +
                "\nAttachments ID: " + attachments +
                "\nCreatedAt: " + this.getCreatedAt() +
                "\nUpdatedAt: " + this.getUpdatedAt() +
                "\nUUID: " + this.getId();
    }
}
