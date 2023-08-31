package pl.wartego.messagelink;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EmailBodyTest {

    @Test
    void getFileFromResourceAsStream() {
        ClassLoader classLoader = EmailBody.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("emailbody.txt");
        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! ");
        }
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);


    }
}