package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class MessageAttachment extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_attachment_message"))
    private Message message;

    @ManyToOne(optional = false)
    @JoinColumn(name = "attachment_id", foreignKey = @ForeignKey(name = "fk_attachment_binary_content"))
    private BinaryContent attachment;
}
