package com.sprint.mission.discodeit.Repository.file;

import java.util.List;
import java.util.Map;

public interface FileRepository <T>{
    void init();

    void load(Map<String, T> data);

    void save(String id);

}
