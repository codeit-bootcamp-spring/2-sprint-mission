package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    public final List<User> data = new ArrayList<>();
    private static JCFUserService getInstance;

    private JCFUserService(){

    }

    public static JCFUserService getInstance(){
        if(getInstance == null){
            getInstance = new JCFUserService();
        }
        return getInstance;
    }


    @Override
    public void save(String nickname) {
        User user = new User(nickname);
        data.add(user);
        System.out.println("유저 생성 완료" +user);
    }

    @Override
    public User findByUser(UUID uuid) {
        for (User u : data) {
            if(u.getId().equals(uuid)) {
                return u;
            }
        }
        System.out.print("[실패]일치하는 회원이 없습니다.  ");
        return null;
    }

    @Override
    public void findAll() {
        if(data.isEmpty()){
            System.out.println("아이디가 존재하지 않습니다");
            return;
        }

        data.forEach(System.out::println);
    }

    @Override
    public void update(UUID uuid, String nickname) {
        if(data.stream().noneMatch(data -> data.getId().equals(uuid))) {
            System.out.println("[실패]수정하려는 아이디가 존재하지 않습니다.");
            return;
        }

        for(User u : data) {
            if(u.getId().equals(uuid)) {
                u.update(nickname);
                System.out.println("[성공]사용자 변경 완료[사용자 아이디: " + u.getId() +
                        ", 닉네임: " + u.getNickname() +
                        ", 변경 시간: " + u.getUpdatedAt() +
                        "]");
            }
        }
    }

    @Override
    public void delete(UUID uuid) {
        boolean removed = data.removeIf(u -> u.getId().equals(uuid));

        if (!removed) {
            System.out.println("[실패]존재하지 않는 사용자");
        } else {
            System.out.println("[성공]사용자 삭제 완료");
        }
    }
}
