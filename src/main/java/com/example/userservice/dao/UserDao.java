package com.example.userservice.dao;

import com.example.userservice.model.User;
import java.util.List;

public interface UserDao {
    User findById(Long id);
    List<User> findAll();
    User save(User user);
    User update(User user);
    void delete(User user);
}
