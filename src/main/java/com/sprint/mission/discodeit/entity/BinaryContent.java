package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private UUID userId;

    private String uploadFileName;
    private String storeFileName;

    public BinaryContent(UUID id, UUID userId, String uploadFileName, String storeFileName) {
        this.id = id;
        this.userId = userId;
        this.setFileNames(uploadFileName, storeFileName);

    }

    public void setFileNames(String uploadFileName, String storeFileName) {
       this.uploadFileName = (uploadFileName != null) ? uploadFileName : "기본 이미지";
       this.storeFileName = (storeFileName != null) ? storeFileName : "기본 이미지";
    }
}
