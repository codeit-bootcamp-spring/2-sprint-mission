package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.entity.Container.Container;

import java.util.List;
/** <h3>서버 레포지토리 </h3><p>
 * 서버마다 카테고리와 채널 리스트를 갖는다. <br>
 * Container 는 카테고리와 채널의 상위 클래스다.<br></p>
 * @JongwonLee
 * @version 1
 */
public class JCFServerRepository implements ServerRepository  {
    private List<Container> list;

    public List<Container> getList() {
        return list;
    }

    public void setList(List<Container> list) {
        this.list = list;
    }

    public void add(Container container) {
        list.add(container);
    }
}
