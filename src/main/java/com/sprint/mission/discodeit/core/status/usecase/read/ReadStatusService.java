package com.sprint.mission.discodeit.core.status.usecase.read;

import org.springframework.stereotype.Service;

@Service
public interface ReadStatusService extends CreateReadStatusUseCase, FindReadStatusUseCase,
    UpdateReadStatusUseCase, DeleteReadStatusUseCase {

}
