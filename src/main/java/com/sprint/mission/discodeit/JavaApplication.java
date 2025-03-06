package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;


// 테스트 버전
public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService jcfUserService = new JCFUserService();

        //등록
        User user1 = new User("Jaeseok");
        jcfUserService.create(user1);
        System.out.println("동록: " + user1.getUserName());

        //조회
        User check = jcfUserService.read(user1.getId()).orElse(null);
        System.out.println("조회: " + check != null ? check.getUserName() : "조회한 사용자는 없습니다");

        //수정
        user1.updateUserName("Raon");
        jcfUserService.update(user1.getId(), user1);
        //수정된 상황 조회
        System.out.println("수정된 사용자: " + jcfUserService.read(user1.getId()).get().getUserName());

        //삭제
        jcfUserService.delete(user1.getId());
        //삭제된 확인
        System.out.println("삭제된 사용자: " + jcfUserService.read(user1.getId()).orElse(null));
    }
}
