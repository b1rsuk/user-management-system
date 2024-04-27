package com.clear.solutions.user.management.system.model;

import com.clear.solutions.user.management.system.model.dto.UserDto;
import com.clear.solutions.user.management.system.service.base.BaseEntity;
import com.clear.solutions.user.management.system.validation.RegexPattern;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
import org.hibernate.annotations.DynamicUpdate;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@DynamicUpdate
public class User implements BaseEntity, BaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @JsonIgnore
    public UserDto convertToDto() {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(this, UserDto.class);
    }

}
