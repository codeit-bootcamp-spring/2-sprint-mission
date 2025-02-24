package com.sprint.mission.view.output;

import com.sprint.mission.model.entity.User;

public class UserOutput {
    public void creatResponse(User user) {

        System.out.println(user.getUsername() + " 님 회원가입이 완료 되었습니다 ~ !");
    }

}
