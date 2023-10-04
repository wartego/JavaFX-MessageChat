package pl.messagechat.messageChat;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

class EmailBodyTest {

    @Test
    static void getFileFromResourceAsStream() {

        InputStream inputStream = EmailBodyTest.class.getClassLoader().getResourceAsStream("/emailHTMLS/emailbody.txt");
        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! ");
        }
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);


    }
}