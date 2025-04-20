package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "message_attachments")
public class MessageAttachment{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_attachment_message"))
    private Message message;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attachment_id", foreignKey = @ForeignKey(name = "fk_attachment_binary_content"))
    private BinaryContent attachment;

    public MessageAttachment(Message message, BinaryContent attachment) {
        this.message = message;
        this.attachment = attachment;
    }
}
