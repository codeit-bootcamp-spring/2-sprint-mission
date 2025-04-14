package com.sprint.discodeit.sprint.domain.entity;

import com.sprint.discodeit.sprint.domain.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users extends BaseUpdatableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    private boolean deleted;

    // 상태 메시지: 1:1 단방향
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_status_id")
    private UsersStatus usersStatus;

    // BinaryContent 연관관계 양방향
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BinaryContent> binaryContents = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadStatus> readStatuses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    public void update(String newUsername, String newEmail, String newPassword) {
        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
        }
    }

    public void softDelete() {
        this.deleted = true;
    }


    // 양방향 연관관계 편의 메서드
    public void addBinaryContent(BinaryContent content) {
        binaryContents.add(content);
        content.setUser(this);
    }

    public void removeBinaryContent(BinaryContent content) {
        binaryContents.remove(content);
        content.setUser(null);
    }
}
