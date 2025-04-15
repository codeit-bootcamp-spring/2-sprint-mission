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

    // 연관 테이블 생성(부모가 관리)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "message_attachments", joinColumns = @JoinColumn(name = "message_id"))
    @Column(name = "attachment_id")
    @OrderBy
    private List<UUID> attachmentIds = new ArrayList<>();

    // 생성자 수정
    public Message(String content, Channel channel, User author, List<UUID> attachmentIds) {
        super();
        this.content = content;
        this.channel = channel;
        this.author = author;
        if (attachmentIds != null) {
            this.attachmentIds = attachmentIds;
        }
    }

    // update 메서드 유지
    public boolean update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }
        return anyValueUpdated;
    }

    // addAttachment/removeAttachment 메서드 유지
    public void addAttachment(UUID attachmentId) {
        if (this.attachmentIds == null) {
            this.attachmentIds = new ArrayList<>();
        }
        if (!this.attachmentIds.contains(attachmentId)) {
            this.attachmentIds.add(attachmentId);
        }
    }

    public void removeAttachment(UUID attachmentId) {
        if (this.attachmentIds != null) {
            this.attachmentIds.remove(attachmentId);
        }
    }
}
