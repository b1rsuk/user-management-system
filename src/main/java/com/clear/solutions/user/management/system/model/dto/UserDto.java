package com.clear.solutions.user.management.system.model.dto;

import com.clear.solutions.user.management.system.model.BaseUser;
import com.clear.solutions.user.management.system.validation.RegexPattern;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@JsonIgnoreProperties(value={ "id" }, allowGetters=true)
public class UserDto implements BaseUser {

    Long id;

    @Email(regexp = RegexPattern.EMAIL.EMAIL_REGEX_PATTERN,
            message = RegexPattern.EMAIL.INVALID_EMAIL_MESSAGE)
    @NotNull
    String email;

    @Size(min = 2, max = 100)
    @Pattern(regexp = RegexPattern.NAME.NAME_REGEX_PATTERN,
            message = RegexPattern.NAME.INVALID_NAME_MESSAGE)
    @NotBlank
    String firstName;

    @Size(min = 2, max = 100)
    @Pattern(regexp = RegexPattern.NAME.NAME_REGEX_PATTERN,
            message = RegexPattern.NAME.INVALID_NAME_MESSAGE)
    @NotBlank
    String lastName;

    @Temporal(TemporalType.DATE)
    @Past(message = "Value must be earlier than current date")
    @NotNull
    Date birthDate;

    @Size(min = 5, max = 86)
    String address;

    @Size(min = 6, max = 14)
    @Pattern(regexp = RegexPattern.PHONE.PHONE_NUMBER_REGEX_PATTERN,
            message = RegexPattern.PHONE.INVALID_PHONE_MESSAGE)
    String phoneNumber;

}
