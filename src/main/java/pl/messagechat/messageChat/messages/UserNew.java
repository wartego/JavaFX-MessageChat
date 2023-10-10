package pl.messagechat.messageChat.messages;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Builder
@Getter
@Setter
public class UserNew implements Serializable {
    private String login;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String verificationCode;

}
