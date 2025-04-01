package com.sprint.mission.discodeit.core.server.usecase;

import org.springframework.stereotype.Service;

@Service
public interface ServerService extends ServerAccessUseCase, CreateServerUseCase, FindServerUseCase,
    DeleteServerUseCase {

}
