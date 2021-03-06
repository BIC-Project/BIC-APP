package com.bic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bic.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

    public User findByUserName(String username);

}
