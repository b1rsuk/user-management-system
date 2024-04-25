package com.clear.solutions.user.management.system.repository;

import com.clear.solutions.user.management.system.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {}
