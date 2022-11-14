package com.example.Library.model.dto;

import com.example.Library.model.entity.UserRole;

public class UserCreateDto {

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String password;
    private UserRole userRole;

    public UserCreateDto(String firstName, String lastName, String email,
                          String address, String password, UserRole userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.password = password;
        this.userRole = userRole;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

}
