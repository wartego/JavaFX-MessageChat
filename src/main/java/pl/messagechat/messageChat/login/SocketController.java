package pl.messagechat.messageChat.login;

import java.io.*;
import java.net.Socket;

public class SocketController {
    private static Socket socket;

    private static ObjectOutputStream oos;
    private static InputStream is;
    private static ObjectInputStream input;
    private static OutputStream outputStream;







    public static Socket getSocket() throws IOException {
        if(socket == null){
            socket = new Socket("localhost",5555);
        }
            return socket;
    }

    public static ObjectOutputStream getOos() throws IOException {
        oos = new ObjectOutputStream(outputStream);
        return oos;
    }

    public static InputStream getIs() throws IOException {
        is = socket.getInputStream();
        return is;
    }

    public static ObjectInputStream getInput() throws IOException {
        input = new ObjectInputStream(is);
        return input;
    }

    public static OutputStream getOutputStream() throws IOException {
        outputStream = socket.getOutputStream();
        return outputStream;
    }
}
