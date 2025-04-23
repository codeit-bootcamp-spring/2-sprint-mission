package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseUpdatableEntity {

    @Setter
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Setter
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Setter
    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "fk_profile"))
    private BinaryContent profile;

    @Setter
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus status;

    @Builder
    public User(String username, String email, String password, BinaryContent profile) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    public void updateProfile(BinaryContent profile) {
        this.profile = profile;
    }
}