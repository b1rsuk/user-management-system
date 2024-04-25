package com.clear.solutions.user.management.system.rule;

import lombok.Getter;

@Getter
public enum SQL_QUERY {

    FIND_ALL_TABLES("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = 'public'"),
    DROP_TABLE_BY_NAME("DROP TABLE %s CASCADE");

    private final String query;

    SQL_QUERY(String query) {
        this.query = query;
    }

}
