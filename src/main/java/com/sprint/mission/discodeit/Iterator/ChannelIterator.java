package com.sprint.mission.discodeit.Iterator;

import com.sprint.mission.discodeit.service.jcf.CategoryAndChannel;

public class ChannelIterator implements Iterator {
    private CategoryAndChannel channel;
    private CategoryAndChannel category;

    public ChannelIterator(CategoryAndChannel category) {
        this.category = category;
    }

    @Override
    public boolean hasNext() {
        if (category.getClass().isInstance(channel)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Object next() {
        return null;
    }
}
