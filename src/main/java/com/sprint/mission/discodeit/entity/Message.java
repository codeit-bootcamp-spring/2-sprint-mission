package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Message extends BaseUpdatableEntity {

    @Lob
    private String content;

    // 다 대 일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", referencedColumnName = "id", nullable = false)
    private Channel channel;

    //다 대 일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User author;
    
    // 1대 다 조인 테이블 생성
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinTable(
        name = "message_attachments",
        joinColumns = @JoinColumn(name = "message_id"),
        inverseJoinColumns = @JoinColumn(name = "attachment_id", unique = true)
    )
    private List<BinaryContent> attachments = new ArrayList<>();


    public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
        super();
        this.content = content;
        this.channel = channel;
        this.author = author;
        if (attachments != null) {
            this.attachments = attachments;
        }
    }

    public boolean update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }
        return anyValueUpdated;
    }

}
