package com.clear.solutions.user.management.system.repository;

import com.clear.solutions.user.management.system.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAllByBirthDateBetween(Date from, Date to);

}
