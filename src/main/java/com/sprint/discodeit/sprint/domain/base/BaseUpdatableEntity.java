package com.sprint.discodeit.sprint.domain.base;

import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Getter
public class BaseUpdatableEntity extends BaseEntity {

    @LastModifiedDate
    private Instant lastModified;

}
