package com.clear.solutions.user.management.system.controller;


import com.clear.solutions.user.management.system.model.User;
import com.clear.solutions.user.management.system.model.dto.UpdateUserDto;
import com.clear.solutions.user.management.system.model.dto.UserDto;
import com.clear.solutions.user.management.system.service.UserService;
import com.clear.solutions.user.management.system.service.filter.BirthDateRangeFilter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        User createdUser = userService.save(userDto);

        return createdUser.convertToDto();
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDto updateUserDto) {
        User createdUser = userService.update(id, updateUserDto);

        return createdUser.convertToDto();
    }

    @GetMapping
    public List<UserDto> findAllByBirthDateBetween(@RequestParam(value = "from", defaultValue = "1970-01-01")
                                     @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate from,
                                     @RequestParam(value = "to", defaultValue = "9999-12-31")
                                     @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate to) {
        BirthDateRangeFilter birthDateRangeFilter = new BirthDateRangeFilter(from, to);
        return userService.findAllByBirthDateBetween(birthDateRangeFilter);
    }

}
