package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Iterator.ChannelIterator;
import com.sprint.mission.discodeit.Iterator.Iterator;
import com.sprint.mission.discodeit.composit.Category;
import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.service.CategoryAndChannelService;

import java.util.LinkedList;

public abstract class CategoryAndChannel extends BaseEntity implements CategoryAndChannelService {
    public CategoryAndChannel(String id, String name) {
        super(id, name);
    }

    @Override
    public LinkedList<CategoryAndChannel> getList() {
        return null;
    }

    @Override
    public void add(CategoryAndChannel channel) {

    }

    @Override
    public void remove(CategoryAndChannel channel) {
    }

    @Override
    public void update(CategoryAndChannel channel, String replaceName) {
    }

    @Override
    public void add(String str) {

    }

    @Override
    public void remove() {

    }

    @Override
    public void update() {

    }

    @Override
    public void print() {

    }

    @Override
    public void printHead() {
    }

    @Override
    public void printCurrent() {

    }

    @Override
    public boolean checkCategory(CategoryAndChannel item) {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
