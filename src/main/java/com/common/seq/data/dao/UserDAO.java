package com.common.seq.data.dao;

import com.common.seq.data.entity.User;

public interface UserDAO {

    public void save(User user);

    public User getUserByEmail(String email);
}
