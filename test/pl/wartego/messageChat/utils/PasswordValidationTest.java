package pl.wartego.messageChat.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidationTest {

    @Test
    void ifHashMatchToPassword() {
        String password = "tomek";
        String passwordHashed = "$2a$10$JDCtc4VFvuNdrIXyvNWtw.W71QaILQJAEyN4xpqzNrJBmnB9olMvi";
        boolean b = PasswordValidation.ifHashMatchToPassword(passwordHashed,password);
        assertTrue(b);
    }
}