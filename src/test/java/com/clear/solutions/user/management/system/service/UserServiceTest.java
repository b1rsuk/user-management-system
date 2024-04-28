package com.clear.solutions.user.management.system.service;

import com.clear.solutions.user.management.system.ValidationUtils;
import com.clear.solutions.user.management.system.model.User;
import com.clear.solutions.user.management.system.model.dto.UserDto;
import com.clear.solutions.user.management.system.rule.DatabaseCleanupRule;
import com.clear.solutions.user.management.system.service.exception.AgeValidateException;
import com.clear.solutions.user.management.system.service.exception.UserNotFoundException;
import com.clear.solutions.user.management.system.service.filter.BirthDateRangeFilter;
import com.clear.solutions.user.management.system.utils.DateUtils;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import jakarta.validation.ConstraintViolationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class, SqlScriptsTestExecutionListener.class})
public class UserServiceTest {

    @Rule
    @Autowired
    public DatabaseCleanupRule databaseCleanupRule;

    @Autowired
    public UserService userService;


    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldFindAllByBirthDateBetween() {
        List<UserDto> expectedUsers = new ArrayList<>();

        UserDto user1 = new UserDto(1L, "example1@example.com", "Ivan", "Petrov", convertToDate("1990-05-15"), "Shevchenko Street, building 10, apartment 5, Kyiv, Ukraine", "+380991234567");
        expectedUsers.add(user1);

        UserDto user2 = new UserDto(2L, "example2@example.com", "Maria", "Ivanova", convertToDate("1985-12-20"), "Lesya Ukrainka Street, building 25, Lviv, Ukraine", "+380982345678");
        expectedUsers.add(user2);

        UserDto user3 = new UserDto(5L, "example5@example.com", "Andrii", "Morozov", convertToDate("1982-09-12"), "Stepan Bandera Street, building 14, Odessa, Ukraine", "+380955678901");
        expectedUsers.add(user3);

        UserDto user4 = new UserDto(6L, "example6@example.com", "Olena", "Kravchuk", convertToDate("1989-11-03"), null, null);
        expectedUsers.add(user4);

        UserDto user5 = new UserDto(8L, "example8@example.com", "Yulia", "Kozlova", convertToDate("1987-08-22"), "Independence Avenue, building 60, Zaporizhzhia, Ukraine", null);
        expectedUsers.add(user5);

        UserDto user6 = new UserDto(10L, "example10@example.com", "Nataliia", "Boiko", convertToDate("1980-02-10"), "Hrushevskoho Street, building 17, Cherkasy, Ukraine", "+380900123456");
        expectedUsers.add(user6);


        LocalDate from = LocalDate.parse("1978-08-08");
        LocalDate to = LocalDate.parse("1990-08-08");
        BirthDateRangeFilter birthDateRangeFilter = new BirthDateRangeFilter(from, to);
        List<UserDto> actual = userService.findAllByBirthDateBetween(birthDateRangeFilter);
        assertEquals(expectedUsers, actual);
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    @ExpectedDatabase(value = "classpath:user_service/expected_for_deleted_user.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldDeleteUser() {
        userService.deleteById(8L);
        assertFalse(userService.userExistsById(8L));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldDeleteNotExistsUser() {
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userService.deleteById(29L));
        assertEquals("User not found with id: 29", userNotFoundException.getMessage());
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    @ExpectedDatabase(value = "classpath:user_service/expected_for_update_user.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldUpdateUser() {
        User user = new User();

        Date birthDate = convertToDate("1978-08-08");
        user.setBirthDate(birthDate);

        user.setPhoneNumber("+380974456789");

        User updatedUser = userService.update(3L, user);

        assertAll(
                () -> assertEquals(birthDate, updatedUser.getBirthDate()),
                () -> assertEquals("+380974456789", updatedUser.getPhoneNumber())
        );
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    @ExpectedDatabase(value = "classpath:user_service/expected_for_create_user.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateUser() {
        User user = new User();
        user.setAddress("789 Oak Street, Somewhere, USA");
        user.setEmail("user@example.com");

        Date birthDateUser = convertToDate("2005-04-25");
        user.setBirthDate(birthDateUser);

        user.setFirstName("Alice");
        user.setLastName("Johnson");
        user.setPhoneNumber("+1234567890");

        User createUser = userService.save(user);
        assertEquals(11L, createUser.getId());
    }

    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    @ExpectedDatabase(value = "classpath:user_service/expected_for_create_user_with_null_optional_fields.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateUserWithNullOptionalFields() {
        User userWithNullOptionalFields = generateTemplateUser();
        userService.save(userWithNullOptionalFields);
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenLessThanRequired() {
        User user = generateTemplateUser();
        LocalDate lessThanNeed = LocalDate.parse("2020-04-25");
        Date futureBirthDateUser = DateUtils.convertLocalDateToDate(lessThanNeed);
        user.setBirthDate(futureBirthDateUser);

        AgeValidateException ageValidateException = assertThrows(AgeValidateException.class, () -> userService.save(user));

        assertEquals("You must be at least 18 years old.", ageValidateException.getMessage());
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenAddressIsTooShort() {
        User user = generateTemplateUser();
        user.setAddress("a");

        ConstraintViolationException exceptionWhenAddressIsTooShort = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("size must be between 5 and 86", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenAddressIsTooShort));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenAddressIsTooLong() {
        User user = generateTemplateUser();
        user.setFirstName("a".repeat(101));

        ConstraintViolationException exceptionWhenAddressIsTooLong = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("size must be between 2 and 100", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenAddressIsTooLong));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenPhoneNumberDoesNotContainPlus() {
        User user = generateTemplateUser();

        user.setPhoneNumber("1234567890");
        ConstraintViolationException exceptionWhenPhoneNumberDoesNotContainPlus = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("Invalid phone number", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenPhoneNumberDoesNotContainPlus));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenPhoneNumberContainsSymbol() {
        User user = generateTemplateUser();

        user.setPhoneNumber("+343434@3443");
        ConstraintViolationException exceptionWhenPhoneNumberContainsSymbol = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("Invalid phone number", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenPhoneNumberContainsSymbol));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenPhoneNumberIsTooShort() {
        User user = generateTemplateUser();

        user.setPhoneNumber("+1234");
        ConstraintViolationException exceptionWhenPhoneNumberIsTooShort = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("size must be between 6 and 14", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenPhoneNumberIsTooShort));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenPhoneNumberIsTooLong() {
        User user = generateTemplateUser();

        user.setPhoneNumber("+123456789012345678901234567890123456789012345678901234567890");
        ConstraintViolationException exceptionWhenPhoneNumberIsTooLong = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("size must be between 6 and 14", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenPhoneNumberIsTooLong));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenEmailMissingSymbol() {
        User user = generateTemplateUser();

        user.setEmail("user2example.com");
        ConstraintViolationException exceptionWhenMissingSymbol = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("Invalid email address", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenMissingSymbol));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenEmailIncompleteDomain() {
        User user = generateTemplateUser();

        user.setEmail("user@example");
        ConstraintViolationException exceptionWhenIncompleteDomain = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("Invalid email address", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenIncompleteDomain));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenEmailMissingTopLevelDomain() {
        User user = generateTemplateUser();

        user.setEmail("user@example.");
        ConstraintViolationException exceptionWhenMissingTopLevelDomain = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("Invalid email address", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenMissingTopLevelDomain));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenFirstNameContainsNumber() {
        User user = generateTemplateUser();

        user.setFirstName("Alex2");
        ConstraintViolationException exceptionWhenFirstNameContainsNumber = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("Invalid name format", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenFirstNameContainsNumber));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenFirstNameContainsSpaces() {
        User user = generateTemplateUser();

        user.setFirstName("John Doe");
        ConstraintViolationException exceptionWhenFirstNameContainsSpaces = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("Invalid name format", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenFirstNameContainsSpaces));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenFirstNameIsEmpty() {
        User user = generateTemplateUser();

        user.setFirstName("   ");
        ConstraintViolationException exceptionWhenFirstNameIsEmpty = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("must not be blank", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenFirstNameIsEmpty));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenFirstNameContainsSpecialCharacters() {
        User user = generateTemplateUser();

        user.setFirstName("John#Doe");
        ConstraintViolationException exceptionWhenFirstNameContainsSpecialCharacters = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("Invalid name format", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenFirstNameContainsSpecialCharacters));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenFirstNameIsTooShort() {
        User user = generateTemplateUser();
        user.setFirstName("a");

        ConstraintViolationException exceptionWhenFirstNameIsTooShort = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("size must be between 2 and 100", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenFirstNameIsTooShort));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenFirstNameIsTooLong() {
        User user = generateTemplateUser();
        user.setFirstName("a".repeat(101));

        ConstraintViolationException exceptionWhenFirstNameIsTooLong = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("size must be between 2 and 100", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenFirstNameIsTooLong));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenLastNameContainsNumber() {
        User user = generateTemplateUser();

        user.setLastName("Doe2");
        ConstraintViolationException exceptionWhenLastNameContainsNumber = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("Invalid name format", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenLastNameContainsNumber));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenLastNameContainsSpaces() {
        User user = generateTemplateUser();

        user.setLastName("John Smith");
        ConstraintViolationException exceptionWhenLastNameContainsSpaces = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("Invalid name format", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenLastNameContainsSpaces));
    }


    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenLastNameContainsSpecialCharacters() {
        User user = generateTemplateUser();

        user.setLastName("Doe#Smith");
        ConstraintViolationException exceptionWhenLastNameContainsSpecialCharacters = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("Invalid name format", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenLastNameContainsSpecialCharacters));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenLastNameIsTooShort() {
        User user = generateTemplateUser();
        user.setLastName("a");

        ConstraintViolationException exceptionWhenLastNameIsTooShort = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("size must be between 2 and 100", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenLastNameIsTooShort));
    }

    @Test
    @Sql(scripts = {"classpath:initial_db.sql", "classpath:user_service/initial_for_user.sql"})
    public void shouldThrowExceptionWhenLastNameIsTooLong() {
        User user = generateTemplateUser();
        user.setLastName("a".repeat(101));

        ConstraintViolationException exceptionWhenLastNameIsTooLong = assertThrows(ConstraintViolationException.class, () -> userService.save(user));

        assertEquals("size must be between 2 and 100", ValidationUtils.extractConstraintViolationsMessage(exceptionWhenLastNameIsTooLong));
    }


    private User generateTemplateUser() {
        User user = new User();
        user.setEmail("user2@example.com");

        Date birthDateUser = DateUtils.convertLocalDateToDate(LocalDate.now().minusYears(28));
        user.setBirthDate(birthDateUser);

        user.setFirstName("Jane");
        user.setLastName("Smith");

        return user;
    }

    private Date convertToDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return DateUtils.convertLocalDateToDate(localDate);
    }
}