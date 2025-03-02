package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserRepository {
     User readuser(String userId);//조회
     boolean registerUserId(User user);//저장
     boolean removeUser(String user); //삭제
     User updateUser(String userId); //업데이트
     List<String> allReadUsers(); //전체조회
     boolean containsUser(String user); //포함확인

}
