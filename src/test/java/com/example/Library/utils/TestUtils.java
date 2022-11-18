package com.example.Library.utils;

import com.example.Library.model.entity.UserRole;

public class TestUtils {

    //BOOK CONSTANTS

    public static final String FIRSTNAME = "Marko";
    public static final String LASTNAME = "Adamovic";
    public static final String TITLE = "Lord of the rings";
    public static final String DESCRIPTION = "Action";
    public static final String UPDATE_BOOK_TITLE = "Na Drini cuprija";
    public static final String UPDATE_DESCRIPTION = "Drama";
    public static Long BOOKDTO_ID = 1L;
    public static final String URL_BOOK_PREFIX = "/book";

    //USER CONSTANTS

    public static String FIRSTNAME_USER = "Marko";
    public static String LASTNAME_USER = "Adamovic";
    public static String EMAIL = "markoadam1995@yahoo.com";
    public static String ADDRESS = "Veternik";
    public static String PASSWORD = "123";
    public static UserRole USERROLE = UserRole.valueOf("USER");
    public static String EMAIL_CREATE = "noviemail@gmail.com";

    public static String UPDATE_FIRSTNAME = "Nikola";
    public static String UPDATE_LASTNAME = "Nikolic";
    public static String UPDATE_EMAIL = "nikolicniko@gmail.com";
    public static String UPDATE_ADDRESS = "NS";
    public static UserRole UPDATE_USERROLE = UserRole.valueOf("USER");
    public static final String URL_USER_PREFIX = "/user";

    //AUTHOR CONSTANTS

    public static final String FIRSTNAME_AUTHOR = "Marko";
    public static final String LASTNAME_AUTHOR = "Adamovic";
    public static final String URL_AUTHOR_PREFIX = "/author";
}
