package com.clear.solutions.user.management.system.service;

import com.clear.solutions.user.management.system.model.BaseUser;
import com.clear.solutions.user.management.system.model.User;
import com.clear.solutions.user.management.system.model.dto.UserDto;
import com.clear.solutions.user.management.system.repository.UserRepository;
import com.clear.solutions.user.management.system.service.base.BaseService;
import com.clear.solutions.user.management.system.service.exception.AgeValidateException;
import com.clear.solutions.user.management.system.service.filter.BirthDateRangeFilter;
import com.clear.solutions.user.management.system.utils.DateUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService extends BaseService<User, CrudRepository<User, Long>> {

    private final UserRepository userRepository;

    private final int minimumAllowedAge;

    public UserService(UserRepository userRepository, @Value("${settings.minimum-allowed-age}") int minimumAllowedAge) {
        super(userRepository);
        this.userRepository = userRepository;
        this.minimumAllowedAge = minimumAllowedAge;
    }

    public List<UserDto> findAllByBirthDateBetween(BirthDateRangeFilter birthDateRangeFilter) {
        Date from = birthDateRangeFilter.getFrom();
        Date to = birthDateRangeFilter.getTo();

        return userRepository.findAllByBirthDateBetween(from, to).stream()
                .map(User::convertToDto)
                .toList();
    }

    public User update(Long id, BaseUser user) {
        User convertedToUser = convertToEntity(user);
        convertedToUser.setId(id);

        return save(convertedToUser);
    }

    public User save(BaseUser user) {
        User convertedToUser = convertToEntity(user);
        return save(convertedToUser);
    }

    @Override
    public User save(User user) {
        validateUser(user);

        return super.save(user);
    }

    private void validateUser(User user) {
        Date birthDate = user.getBirthDate();

        if (birthDate != null) {
            validateAge(birthDate);
        }
    }

    private void validateAge(Date birthDate) {
        if (!isAgeAllowed(birthDate)) {
            throw new AgeValidateException(minimumAllowedAge);
        }
    }

    private boolean isAgeAllowed(Date birthDate) {
        return DateUtils.convertToYears(birthDate) >= minimumAllowedAge;
    }

    public User convertToEntity(BaseUser userDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userDto, User.class);
    }

}
