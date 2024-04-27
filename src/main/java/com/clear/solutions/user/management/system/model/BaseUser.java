package com.clear.solutions.user.management.system.model;

import java.util.Date;

public interface BaseUser {
    Long getId();
    String getEmail();
    String getFirstName();
    String getLastName();
    Date getBirthDate();
    String getAddress();
    String getPhoneNumber();
}
