package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.common.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message extends BaseUpdatableEntity implements Persistable<UUID> {

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    // TODO 일대다 단방향 말고 다대일 양방향 활용
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "message_attachments")
    private List<BinaryContent> attachments = new ArrayList<>();

    @Transient
    private boolean isNew = true;

    protected Message() {
    }

    public static Message createMessage(Channel channel, User author, String content) {

        Message message = new Message();
        message.setChannel(channel);
        message.setAuthor(author);
        message.setContent(content);

        return message;
    }

    public void update(String newContent) {
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
        }
    }

    public void addBinaryContent(BinaryContent attachment) {
        this.attachments.add((attachment));
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PrePersist
    public void markNotNew() {
        this.isNew = false;
    }
}
