package com.sprint.discodeit.sprint.domain.entity;

import com.sprint.discodeit.sprint.domain.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class BinaryContent extends BaseUpdatableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileType; // 이미지, PDF 등 파일 타입
    private Long fileSize;
    private String fileName;

    // Users 엔티티와의 연관관계: ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK 컬럼 이름
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;

    @Builder
    public BinaryContent(Long fileSize, String fileType, String fileName) {
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileName = fileName;
    }

    public void update(String fileType, Long fileSize) {
        if (fileType != null && fileSize != null) {
            this.fileSize = fileSize;
            this.fileType = fileType;
        }
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
