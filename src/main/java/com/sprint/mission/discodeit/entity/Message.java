package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class Message extends BaseUpdatableEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Channel channel;
    private User user;
    private String context;
    private List<BinaryContent> attachments;

    public Message(Channel channel, User user, String context, List<BinaryContent> attachments) {
        this.channel = channel;
        this.user = user;
        this.context = context;
        this.attachments = attachments;
    }

    public void updateContext(String context) {
        this.context = context;
    }
}
