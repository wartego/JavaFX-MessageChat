package pl.messagechat.messageChat.messages;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserOnlyLogin implements Serializable {

    private String login;
    private String password;
    private MessageType messageType;

}
