package pl.messagechat.messageChat.messages;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class User implements Serializable {

    private String userName;
    private Status status;
    private String picture;
    public User() {
    }
}
