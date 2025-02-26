package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.Map;

/** <h3>유저 레포지토리 </h3><p>
 * 유저마다 서버 리스트를 갖는다. <br>
 * 유저는 개인 메시지함을 받는다.<br>
 * </p>
 * @JongwonLee
 * @version 1
*/
public abstract class UserRepository {
    private List<Server> serverList;
    private List<Message> messageList;

    public void setServerList(List<Server> serverList) {
        this.serverList = serverList;
    }

    public List<Server> getServerList() {
        return serverList;
    }



    public void add(Server server) {
        serverList.add(server);
    }


}
