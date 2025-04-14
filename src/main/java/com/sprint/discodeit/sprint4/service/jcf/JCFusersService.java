package com.sprint.discodeit.sprint4.service.jcf;

import com.sprint.discodeit.sprint5.domain.entity.users;
import com.sprint.discodeit.sprint4.repository.jcf.JCFusersRepository;
import com.sprint.discodeit.sprint4.service.usersService;

import java.util.*;

public class JCFusersService implements usersService {
    private final JCFusersRepository jcfusersRepository;

    public JCFusersService(JCFusersRepository jcfusersRepository) {
        this.jcfusersRepository = jcfusersRepository;
    }


    @Override
    public users create(String usersname, String email, String password) {
        users users = new users(usersname, email, password);
        jcfusersRepository.save(users);
        return users;
    }

    @Override
    public users find(UUID usersId) {
      return jcfusersRepository.findById(usersId).orElseThrow(() -> new NoSuchElementException(usersId.toString() + " 없는 회원 입니다"));
    }

    @Override
    public List<users> findAll() {
        return jcfusersRepository.findByAll();
    }

    @Override
    public users update(UUID usersId, String newusersname, String newEmail, String newPassword) {
        users users = jcfusersRepository.findById(usersId).orElseThrow(() -> new NoSuchElementException(usersId.toString() + " 없는 회원 입니다"));
        users.update(newusersname, newEmail, newPassword);
        return users;
    }

    @Override
    public void delete(UUID usersId) {
        jcfusersRepository.delete(usersId);
    }
}
