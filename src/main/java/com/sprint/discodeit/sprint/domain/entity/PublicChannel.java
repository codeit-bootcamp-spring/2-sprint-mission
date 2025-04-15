package com.sprint.discodeit.sprint.domain.entity;

import static lombok.AccessLevel.PROTECTED;

import com.sprint.discodeit.sprint.domain.ChannelType;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PublicChannel extends Channel{

    private PublicChannel(String name, String description) {
        super(ChannelType.PRIVATE, name, description);
    }

    public static PublicChannel create(String name, String description) {
        return new PublicChannel(name, description);
    }
}
