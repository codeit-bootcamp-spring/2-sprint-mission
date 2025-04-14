package com.sprint.discodeit.sprint.domain.entity;

import com.sprint.discodeit.sprint.domain.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinaryContent extends BaseUpdatableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileType; // 이미지, PDF 등 파일 타입
    private String filePath;

    // Users 엔티티와의 연관관계: ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK 컬럼 이름
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id'")
    private Message message;

    public void update(String fileType, String filePath) {
        if (fileType != null && filePath != null) {
            this.filePath = filePath;
            this.fileType = fileType;
        }
    }

    // 양방향 연관관계 편의 메서드
    public void setUser(Users user) {
        this.user = user;
    }
}
