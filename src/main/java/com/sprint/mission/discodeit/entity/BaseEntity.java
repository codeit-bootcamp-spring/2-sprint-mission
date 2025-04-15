package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, updatable = false)
    protected final UUID id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected final Instant createdAt;

    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }
}
