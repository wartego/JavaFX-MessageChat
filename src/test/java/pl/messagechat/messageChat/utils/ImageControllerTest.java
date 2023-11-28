package pl.messagechat.messageChat.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageControllerTest {

    @Test
    void checkIfFileAlreadyExist() {
        String fileName = "wartego.png";
        boolean exist = ImageController.checkIfFileAlreadyExist(fileName);
        assertEquals(true,exist);

    }
}