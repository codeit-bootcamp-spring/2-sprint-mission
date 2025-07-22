package com.sprint.mission.discodeit.core.recover.service;

import java.util.UUID;

public interface RecoverService {

  void write(UUID id, String message);

}
