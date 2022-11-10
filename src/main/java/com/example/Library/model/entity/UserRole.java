package com.example.Library.model.entity;

public enum UserRole {

    ADMINISTRATOR("ADMINISTRATOR"),
    USER("USER");

    private final String name;

    UserRole(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
