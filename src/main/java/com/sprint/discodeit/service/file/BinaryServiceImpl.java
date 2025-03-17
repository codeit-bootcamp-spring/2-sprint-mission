package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.entity.BinaryContent;
import com.sprint.discodeit.service.BinaryService;
import org.springframework.stereotype.Service;

@Service
public class BinaryServiceImpl implements BinaryService {

    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://your-storage.com/default-profile.png";

    @Override
    public BinaryContent createProfileImage(String imgUrl) {
        if (imgUrl == null || imgUrl.isBlank()) {
            return new BinaryContent("이미지", DEFAULT_PROFILE_IMAGE_URL); // 기본 이미지 설정
        }
        return new BinaryContent("이미지", imgUrl); // 사용자가 업로드한 이미지
    }
}
