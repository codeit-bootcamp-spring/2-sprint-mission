package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseUpdatableEntity {

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.ROLE_USER;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private BinaryContent profile;

    public User(String username, String email, String password,
        BinaryContent nullableProfileObject) {
        super();
        this.email = email;
        this.password = password;
        this.username = username;
        this.profile = nullableProfileObject;
        this.role = Role.ROLE_USER;
    }

    public User(String username, String email, String password,
        BinaryContent nullableProfileObject, Role role) {
        super();
        this.email = email;
        this.password = password;
        this.username = username;
        this.profile = nullableProfileObject;
        this.role = role != null ? role : Role.ROLE_USER;
    }
}



