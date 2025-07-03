package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

    @Lob
    @Column(name = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", referencedColumnName = "id", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = true)
    private User author;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private List<BinaryContent> attachments = new ArrayList<>();

    public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
        super();
        this.content = content;
        this.channel = channel;
        this.author = author;
        this.attachments = attachments != null ? attachments : new ArrayList<>();
    }

    public boolean update(String newContent) {
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            return true;
        }
        return false;
    }
}
