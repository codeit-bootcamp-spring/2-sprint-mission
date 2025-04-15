package com.sprint.mission.discodeit.entity;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_attachments")
@Getter
@NoArgsConstructor
public class MessageAttachment {

  @EmbeddedId
  private MessageAttachmentId id;

  @ManyToOne
  @MapsId("messageId")
  @JoinColumn(name = "message_id")
  private Message message;

  @ManyToOne
  @MapsId("attachmentId")
  @JoinColumn(name = "attachment_id")
  private BinaryContent binaryContent;

}
