package pl.messagechat.messageChat.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordValidation {

    //hashing password under registration form
    public static String hashPasswordUnderRegistration(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt(10));
    }

    //encryption password from user under login
    public static String hashPasswordUnderLogin(String password){
        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt(10));
        if(BCrypt.checkpw(password,hashedPassword)){
            System.out.println("Password Match");
            return hashedPassword;
        } else {
            System.out.println("No match");
            return "x";
        }
    }
//compare hashed password from database and user under login
    public static boolean ifHashMatchToPassword(String hashedPassword, String password ){
        return BCrypt.checkpw(password, hashedPassword);
    }
}
