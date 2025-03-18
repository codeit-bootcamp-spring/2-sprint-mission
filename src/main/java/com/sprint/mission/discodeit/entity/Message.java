package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3L;
    private final UUID senderId;
    private String content;
    private final UUID channelId;
    private boolean isEdited;
    private List<UUID> attachmentIds;

    public Message(UUID senderId, String content, UUID channelId) {
        super();
        validateContent(content);
        this.senderId = senderId;
        this.content = content;
        this.channelId = channelId;
        this.isEdited = false;
        this.attachmentIds = new ArrayList<>();
    }

    public boolean isEdited() {
        return this.isEdited;
    }

    public void updateContent(String newContent) {
        validateContent(newContent);
        this.content = newContent;
        this.isEdited = true;
        super.updateUpdatedAt();
    }

    public static void validateContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content는 null 이거나 길이가 0일 수 없다!!!");
        }
    }

    public void addAttachment(UUID attachmentId) {
        this.attachmentIds.add(attachmentId);
    }

    public void updateAttachmentIds(List<UUID> attachmentIds) {
        this.attachmentIds = new ArrayList<>(attachmentIds); // new ArrayList로 생성해야 파라미터로 들어온 attachmentIds의 외부에서로부터의 변경이 Message entity의 attachmentIds에 영향을 미치지 않음.
    }

    public void deleteAttachment(UUID attachmentId) {
        this.attachmentIds.remove(attachmentId);
    }

    @Override
    public String toString() {
        return "\nMessage\n"
                + "senderId: " + this.senderId + "\n"
                + "content: " + this.content + "\n"
                + "channelId: " + this.channelId + "\n"
                + "isEdited: " + this.isEdited + "\n"
                + super.toString() + "\n";
    }
}
