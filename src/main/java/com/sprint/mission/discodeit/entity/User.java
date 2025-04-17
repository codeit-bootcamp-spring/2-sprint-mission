package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.service.TimeFormatter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseUpdatableEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @OneToOne
    @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "fk_profile"))
    private BinaryContent profile;


    public User(String username, String password, String email, BinaryContent profile) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.profile = profile;
    }

    protected User() {
    }

    public void update(String username, String password, String email, BinaryContent profile) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "User{" +
            "username='" + username + '\'' +
            ", password='****'" +
            ", email='" + email + '\'' +
            ", id=" + id +
            ", createdAt=" + TimeFormatter.formatTimestamp(createdAt) +
            ", updatedAt=" + TimeFormatter.formatTimestamp(updatedAt) +
            '}';
    }
}
