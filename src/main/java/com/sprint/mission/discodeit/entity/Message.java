package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import lombok.Getter;
import java.util.List;

@Entity
@Table(name = "messages")
@Getter
public class Message extends BaseUpdatableEntity {

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_author"))
    private User author;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false, foreignKey = @ForeignKey(name = "fk_channel"))
    private Channel channel;

    @Column(columnDefinition = "text")
    private String content;

    @ManyToMany
    @JoinTable(
        // 중간 테이블
        name = "message_attachments",
        joinColumns = @JoinColumn(name = "message_id"),
        inverseJoinColumns = @JoinColumn(name = "attachment_id"))
    private List<BinaryContent> attachments = new ArrayList<>();

    protected Message() {
    }

    public Message(User author, Channel channel, String content, List<BinaryContent> attachments) {
        this.author = author;
        this.channel = channel;
        this.content = content;
        this.attachments = attachments;
    }

    public void update(String content, List<BinaryContent> attachments) {
        this.content = content;
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + id +
            ", authorId=" + (author != null ? author.getId() : null) +
            ", channelId=" + (channel != null ? channel.getId() : null) +
            ", content='" + content + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", attachments=" + (attachments != null ? attachments.size() : 0) + " files" +
            '}';
    }
}
