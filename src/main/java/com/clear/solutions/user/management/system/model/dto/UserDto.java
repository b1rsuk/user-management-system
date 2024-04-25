package com.clear.solutions.user.management.system.model.dto;

import com.clear.solutions.user.management.system.validation.RegexPattern;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class UserDto {

    Long id;

    @Email(regexp = RegexPattern.EMAIL_REGEX_PATTERN)
    String email;

    @Size(max = 100)
    @Pattern(regexp = RegexPattern.NAME_REGEX_PATTERN)
    String firstName;

    @Size(max = 100)
    @Pattern(regexp = RegexPattern.NAME_REGEX_PATTERN)
    String lastName;

    @Temporal(TemporalType.DATE)
    @Past(message = "Value must be earlier than current date")
    Date birthDate;

    @Size(max = 86)
    String address;

    @Pattern(regexp = RegexPattern.PHONE_NUMBER_REGEX_PATTERN)
    String phoneNumber;

}
