package com.sprint.mission.discodeit.Iterator;

import com.sprint.mission.discodeit.entity.Container.Container;

public class ChannelIterator implements Iterator {
    private Container channel;
    private Container category;

    public ChannelIterator(Container category) {
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
