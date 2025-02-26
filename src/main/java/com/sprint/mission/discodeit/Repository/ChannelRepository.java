package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
/** <h3>채널 레포지토리 </h3><p>
 * 채널마다 메시지 리스트를 갖는다. <br>
 * <br></p>
 * @JongwonLee
 * @version 1
 */
public class ChannelRepository {
    private List<Message> list;

    public void setList(List<Message> list) {
        this.list = list;
    }

    public List<Message> getMessageList() {
        return list;
    }

    public void add(Message message) {
        list.add(message);
    }

}
