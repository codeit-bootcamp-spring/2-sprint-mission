package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

//유저 CRUD
public interface UserService {
    void createdUser(User user); //등록
    void readUser(String userId); //조회(단건)
    void readAllUsers();//모든 유저조회
    boolean updateUser(String userId, String update);//수정(조회)
    void deleteUser(String userID);//삭제 및 반환


}
