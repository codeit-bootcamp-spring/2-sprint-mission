package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID mid;
    private final Long messageCreateAt;
    private Long messageUpdateAt;
    private UUID cid;
    private UUID uid;
    private String messageContent;

    public Message(UUID cid, UUID uid, String messageContent){
        this.mid = UUID.randomUUID();
        this.messageCreateAt = System.currentTimeMillis();
        this.cid = cid;
        this.uid = uid;
        this.messageContent = messageContent;
    }


}
