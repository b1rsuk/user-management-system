package com.clear.solutions.user.management.system.service;

import com.clear.solutions.user.management.system.model.User;
import com.clear.solutions.user.management.system.repository.UserRepository;
import com.clear.solutions.user.management.system.service.exception.AgeValidateException;
import com.clear.solutions.user.management.system.utils.DateUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final int minimumAllowedAge;

    public UserService(UserRepository userRepository, @Value("${settings.minimum-allowed-age}") int minimumAllowedAge) {
        this.userRepository = userRepository;
        this.minimumAllowedAge = minimumAllowedAge;
    }

    public User save(User user) {
        validateUser(user);

        return userRepository.save(user);
    }

    private void validateUser(User user) {
        Date birthDate = user.getBirthDate();
        validateAge(birthDate);
    }

    private void validateAge(Date birthDate) {
        if (isAgeAllowed(birthDate)) {
            return;
        }

        throw new AgeValidateException(minimumAllowedAge);
    }

    private boolean isAgeAllowed(Date birthDate) {
        return DateUtils.convertToYears(birthDate) >= minimumAllowedAge;
    }
}
