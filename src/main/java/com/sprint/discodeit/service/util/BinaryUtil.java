package com.sprint.discodeit.service.util;

import com.sprint.discodeit.domain.entity.BinaryContent;
import org.springframework.stereotype.Service;

@Service
public class BinaryUtil {

    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://your-storage.com/default-profile.png";

    public BinaryContent createProfileImage(String imgUrl) {
        if (imgUrl == null || imgUrl.isBlank()) {
            return new BinaryContent("이미지", DEFAULT_PROFILE_IMAGE_URL); // 기본 이미지 설정
        }
        return new BinaryContent("이미지", imgUrl); // 사용자가 업로드한 이미지
    }
}
