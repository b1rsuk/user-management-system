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

    @Email(regexp = RegexPattern.EMAIL.EMAIL_REGEX_PATTERN,
           message = RegexPattern.EMAIL.INVALID_EMAIL_MESSAGE)
    String email;

    @Size(max = 100)
    @Pattern(regexp = RegexPattern.NAME.NAME_REGEX_PATTERN,
             message = RegexPattern.NAME.INVALID_NAME_MESSAGE)
    String firstName;

    @Size(max = 100)
    @Pattern(regexp = RegexPattern.NAME.NAME_REGEX_PATTERN,
             message = RegexPattern.NAME.INVALID_NAME_MESSAGE)
    String lastName;

    @Temporal(TemporalType.DATE)
    @Past(message = "Value must be earlier than current date")
    Date birthDate;

    @Size(max = 86)
    String address;

    @Pattern(regexp = RegexPattern.PHONE.PHONE_NUMBER_REGEX_PATTERN,
             message = RegexPattern.PHONE.INVALID_PHONE_MESSAGE)
    String phoneNumber;

}
