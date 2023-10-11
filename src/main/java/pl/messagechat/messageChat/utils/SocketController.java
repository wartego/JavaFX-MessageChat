package pl.messagechat.messageChat.utils;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;


public class SocketController {
    private static Socket socket;

    private static ObjectOutputStream oos;
    private static ObjectInputStream input;
    private static InputStream is;
    private static OutputStream outputStream;
    static Logger logger = LoggerFactory.getLogger(SocketController.class);



public static boolean createSocketConnection()  {
    try {
        if (socket == null) {
            socket = new Socket("localhost", 5555);
        }
        if(outputStream == null){
            outputStream = socket.getOutputStream();
        }
        if(oos == null){
            oos = new ObjectOutputStream(outputStream);
        }
        if(is == null){
            is = socket.getInputStream();
        }
        if(input == null){
            input = new ObjectInputStream(is);
        }

        return true;

    } catch (IOException exception){
        logger.error("Socket Connection Failure, something goes wrong");
        return false;
    }
}
public static void closeSocketConnection(){
    try {
        if (socket != null) {
            socket.close();
        }
        if (oos != null) {
            oos.close();
        }
        if (is != null) {
            is.close();
        }
        if (input != null) {
            input.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    } catch (IOException exception){
        logger.error("Something goes wrong during close Socket connection!");
    }
}

    public static Socket getSocket() {
        return socket;
    }

    public static ObjectOutputStream getOos() {
        return oos;
    }

    public static InputStream getIs() {
        return is;
    }

    public static ObjectInputStream getInput() {
        return input;
    }

    public static OutputStream getOutputStream() {
        return outputStream;
    }

    public static Logger getLogger() {
        return logger;
    }
}
