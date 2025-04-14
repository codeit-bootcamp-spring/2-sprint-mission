package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor
public class Message extends BaseUpdatableEntity {

    @Setter
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false, foreignKey = @ForeignKey(name = "fk_message_channel"))
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_message_author"))
    private User author;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageAttachment> attachments = new ArrayList<>();

    @Builder
    public Message(String content, Channel channel, User author) {
        super();
        this.content = content;
        this.channel = channel;
        this.author = author;
    }

    public void addAttachment(BinaryContent attachment) {
        MessageAttachment messageAttachment = new MessageAttachment(this, attachment);
        this.attachments.add(messageAttachment);
    }

    public void removeAttachment(MessageAttachment attachment) {
        this.attachments.remove(attachment);
    }
}
