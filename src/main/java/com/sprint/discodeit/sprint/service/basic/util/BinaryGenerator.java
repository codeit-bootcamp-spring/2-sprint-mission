package com.sprint.discodeit.sprint.service.basic.util;

import com.sprint.discodeit.sprint.domain.entity.BinaryContent;
import org.springframework.stereotype.Service;

@Service
public class BinaryGenerator {

    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://your-storage.com/default-profile.png";

    public BinaryContent createProfileImage(String imgUrl) {
        String finalUrl = null;

        if (imgUrl == null || imgUrl.isBlank()) {
            finalUrl = DEFAULT_PROFILE_IMAGE_URL;
        } else {
            finalUrl = imgUrl;
        }

        return BinaryContent.builder()
                .fileType("이미지")
                .filePath(finalUrl)
                .build();
    }
}
