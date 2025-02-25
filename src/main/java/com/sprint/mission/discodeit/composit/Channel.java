package com.sprint.mission.discodeit.composit;

import com.sprint.mission.discodeit.Iterator.Iterator;
import com.sprint.mission.discodeit.entity.Message;

import java.util.ArrayList;
import java.util.Date;


public class Channel extends CategoryAndChannel {
    private ArrayList<Message> messageList;

    public Channel(String id, String name) {
        super(id, name);
        messageList = new ArrayList<>();
    }

    @Override
    public void addChannel(CategoryAndChannel channel) {
    }

    @Override
    public void removeChannel(CategoryAndChannel channel) {
    }

    @Override
    public void updateChannel(CategoryAndChannel channel) {
    }

    @Override
    public void printCurrentCategoryAndChannel() {

    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + id + '\'' +
                ",\nname='" + name + '\'' +
                ",\ncreatedAt=" + dayTime.format(new Date(createdAt)) +
                ",\nupdatedAt=" + dayTime.format(new Date(updatedAt)) +
                '}';
    }
}
