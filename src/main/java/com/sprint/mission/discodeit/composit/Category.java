package com.sprint.mission.discodeit.composit;

import com.sprint.mission.discodeit.service.CategoryAndChannelService;
import com.sprint.mission.discodeit.service.jcf.CategoryAndChannel;

public class Category extends CategoryAndChannel {

    public Category(String id, String name) {
        super(id, name);
    }


    @Override
    public boolean checkCategory(CategoryAndChannelService item) {
        return false;
    }
}
