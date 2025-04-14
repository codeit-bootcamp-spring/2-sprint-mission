package com.sprint.discodeit.sprint.domain.entity;

import com.sprint.discodeit.sprint.domain.base.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatus extends BaseUpdatableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID usersId;
    private UUID channelId;
    private Instant lastReadMessageTime;
    // True 읽음, False 안 읽음
    private Boolean readCheck;


    public void readUpdate(UUID channelId, Boolean readCheck, UUID usersId) {
        if(channelId != null || readCheck != null || usersId != null) {
            this.channelId = channelId;
            this.readCheck = readCheck;
            this.usersId = usersId;
            this.lastReadMessageTime = Instant.now();
        }
    }

}
