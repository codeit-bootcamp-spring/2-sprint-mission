package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "message_attachments")
@Getter
@NoArgsConstructor
public class MessageAttachment {

    @Id
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false, foreignKey = @ForeignKey(name = "fk_attachment_message"))
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_attachment_binary"))
    private BinaryContent attachment;

    public MessageAttachment(Message message, BinaryContent attachment) {
        this.uuid = UUID.randomUUID();
        this.message = message;
        this.attachment = attachment;
    }
}