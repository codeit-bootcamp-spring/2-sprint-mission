package com.sprint.mission.discodeit.core.content.usecase;

import org.springframework.stereotype.Service;


@Service
public interface BinaryContentService extends CreateBinaryContentUseCase, FindBinaryContentUseCase,
    UpdateBinaryContentUseCase, DeleteBinaryContentUseCase {

}
