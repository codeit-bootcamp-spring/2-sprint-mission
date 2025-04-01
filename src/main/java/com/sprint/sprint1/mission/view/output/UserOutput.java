package com.sprint.sprint1.mission.view.output;

import com.sprint.sprint1.mission.model.entity.User;
import java.util.List;
import java.util.Optional;

public class UserOutput {

    public void creatOutput(User user) {
        System.out.println(user.getUsername().toString() + " 님 회원가입이 완료 되었습니다 ~ !");
    }

    public void updatedOutput() {
        System.out.println(" 사용자 정보가 업데이트되었습니다.");
    }

    public void allOutput(List<User> users) {
        System.out.println("등록된 사용자 목록: ");
        for (User user : users) {
            System.out.println(user.getUsername());
        }
    }

    public void getEmailOutput(Optional<User> optionUser) {
        if (optionUser == null || !optionUser.isPresent()) {
            System.out.println("회원 정보를 찾을 수 없습니다 ");
        } else {
            User user = optionUser.get();
            System.out.println(user.getUsername().toString() +" 님 찾았습니다");
        }
    }
}
