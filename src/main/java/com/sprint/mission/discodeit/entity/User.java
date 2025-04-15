package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table
public class User extends BaseUpdatableEntity {

    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    //1대 1 user -> profile
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private BinaryContent profile;

    // 외래키를 가진 status를 주인으로 설정(양방향)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserStatus userStatus;


    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.username = email.split("@")[0];
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
        if (userStatus != null && userStatus.getUser() != this) {
            userStatus.setUser(this);
        }
    }


}



