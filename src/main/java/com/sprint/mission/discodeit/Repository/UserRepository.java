package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Server;

import java.util.LinkedList;
import java.util.List;

/** <h3>유저 레포지토리 </h3><p>
 * 유저마다 서버 리스트를 갖는다. <br>
 * <br></p>
 * @JongwonLee
 * @version 1
*/
public abstract class UserRepository {
    private List<Server> list;

    public void setList(List<Server> list) {
        this.list = list;
    }

    public List<Server> getList() {
        return list;
    }

    public void add(Server server) {
        list.add(server);
    }
}
