package com.example.Library.utils;

import com.example.Library.model.entity.UserRole;

import java.time.LocalDate;

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
    public static String EMAIL = "adam1995@gmail.com";
    public static String EMAIL2 = "drugimail@gmail.com";
    public static String ADDRESS = "Veternik";
    public static String PASSWORD = "123";
    public static UserRole USERROLE_USER = UserRole.valueOf("USER");
    public static UserRole USERROLE_ADMIN = UserRole.valueOf("ADMINISTRATOR");
    public static String EMAIL_CREATE = "noviemail@gmail.com";

    public static String UPDATE_FIRSTNAME = "Nikola";
    public static String UPDATE_LASTNAME = "Nikolic";
    public static String UPDATE_EMAIL = "nikolicniko@gmail.com";
    public static String UPDATE_ADDRESS = "NS";
    public static UserRole UPDATE_USERROLE = UserRole.valueOf("USER");
    public static final String URL_USER_PREFIX = "/user";

    //AUTHOR CONSTANTS

    public static final String FIRSTNAME_AUTHOR = "Marko";
    public static final String FIRSTNAME_AUTHOR_DTO = "Maarkan";
    public static final String LASTNAME_AUTHOR = "Adamovic";
    public static final String LASTNAME_AUTHOR_DTO = "Adaamnam";
    public static final String URL_AUTHOR_PREFIX = "/author";

    //BOOKCOPY

    public static final String IDENTIFICATION = "aaa10";
    public static final Boolean IS_RENTED = false;
    public static final String URL_BOOKCOPY_PREFIX = "/bookCopy";
    public static final String IDENTIFICATION_UPDATE = "aaa222";

    //BOOK RENTAL
    public static final LocalDate DATE_START = null;
    public static final LocalDate DATE_END = null;
    public static final String URL_BOOKRENTAL_PREFIX = "/bookRental";
    public static final String CREATE_BOOKRENTAL_URL = "/book/{bookId}/user/{userId}";
    public static final String END_BOOKRENTAL_URL = "/book/{bookId}/bookCopy/{bookCopyId}";
    public static final String ACTIVE_RENTS = "/activeRents";
    public static final String CLOSED_RENTS = "/closedRents";


    // EXCEPTION

    public static final String NOT_FOUND = "not found";

}
