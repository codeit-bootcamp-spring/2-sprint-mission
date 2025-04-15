package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class MessageAttachment extends BaseEntity {

    @ManyToOne(optional = false) // 메시지가 삭제되면 첨부파일도 삭제됨.
    @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_attachment_message"))
    private Message message;

    @OneToOne(optional = false) // 첨부파일이 삭제되면 연결도 삭제됨.
    @JoinColumn(name = "attachment_id", foreignKey = @ForeignKey(name = "fk_attachment_binary_content"))
    private BinaryContent attachment;
}
