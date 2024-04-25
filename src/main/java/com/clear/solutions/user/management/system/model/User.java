package com.clear.solutions.user.management.system.model;

import com.clear.solutions.user.management.system.validation.RegexPattern;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
