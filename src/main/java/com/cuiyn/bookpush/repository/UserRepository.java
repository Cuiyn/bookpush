package com.cuiyn.bookpush.repository;

import java.util.List;
import java.util.Optional;

import com.cuiyn.bookpush.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByUserName(String userName);
    void deleteByUserName(String userName);

    @Override
    Optional<User> findById(Integer integer);
}
