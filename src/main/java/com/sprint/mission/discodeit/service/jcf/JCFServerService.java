package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.composit.Category;
import com.sprint.mission.discodeit.composit.CategoryAndChannel;
import com.sprint.mission.discodeit.composit.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.ServerService;

import java.util.LinkedList;

public class JCFServerService extends Server implements ServerService {
    private CategoryAndChannel baseCategory;
    private Channel head;

    public JCFServerService(String id, String name) {
        super(id, name);
        baseCategory = new Category("B1","BaseCategory");
        head = new Channel("BC1","BaseChannel");
        baseCategory.add(head);
    }

    public void print() {
        baseCategory.printCurrent();
    }

    public void addChannel(String name) {
        //여긴 추상클래스로 만든 뒤, 구체 클래스에서 static count해서 값을 매긴 뒤 넘길 예정
        String test = "Test";
        baseCategory.add(new Channel(test, name));
    }

    public void update(String targetName, String replaceName) {
        LinkedList<CategoryAndChannel> list = baseCategory.getList();
        for (CategoryAndChannel item : list) {
            if (item.getName() == targetName) {
                baseCategory.update(item,replaceName);
            }
        }
    }

    public void remove(String targetName) {
        LinkedList<CategoryAndChannel> list = baseCategory.getList();
        for (CategoryAndChannel item : list) {
            if (item.getName() == targetName) {
                baseCategory.remove(item);
                return;
            }
        }
        System.out.println("채널 삭제 실패");
    }
}
