package pl.messagechat.messageChat.chat;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.messagechat.messageChat.login.LoginController;
import pl.messagechat.messageChat.login.SocketController;
import pl.messagechat.messageChat.messages.Message;
import pl.messagechat.messageChat.messages.MessageType;
import pl.messagechat.messageChat.messages.Status;
import pl.messagechat.messageChat.messages.UserOnlyLogin;

public class Listener implements Runnable{
    private static final String HASCONNECTED = "has connected";

    private static String picture;
    public String hostname;
    public int port;
    private Socket socket;
    public static String username;
    public ChatController controller;
    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;
    Logger logger = LoggerFactory.getLogger(Listener.class);

    public Listener(String hostname, int port, String username, String picture, ChatController controller) {
        this.hostname = hostname;
        this.port = port;
        Listener.username = username;
        Listener.picture = picture;
        this.controller = controller;

    }

    @Override
    public void run() {
        try {
            socket = new Socket("localhost",5555);
            LoginController.getInstance().showScene();
            outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
        } catch (IOException e) {
            LoginController.getInstance().showErrorDialog("Could not connect to server");
            logger.error("Could not Connect");
        }
        logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());

            try {
                connect();
                logger.info("Socket in and out ready");
                while(socket.isConnected()){
                    Message message = null;
                    message = (Message) input.readObject();
                    if (message != null) {
                        logger.debug("Message recieved:" + message.getMsg() + " MessageType:" + message.getType() + "Name:" + message.getName());
                        switch (message.getType()) {
                            case USER, VOICE:
                                controller.addToChat(message);
                                break;
                            case NOTIFICATION:
                                controller.newUserNotification(message);
                                break;
                            case SERVER:
                                controller.addAsServer(message);
                                break;
                            case CONNECTED:
                                controller.setUserList(message);
                                break;
                            case DISCONNECTED:
                                controller.setUserList(message);
                                break;
                            case STATUS:
                                controller.setUserList(message);
                                break;
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
               // controller.logoutScene(new ActionEvent());
                e.printStackTrace();
            }
        }


    private static void connect() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.CONNECTED);
        createMessage.setMsg(HASCONNECTED);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
    }

    private static void firstLoginSendToServer() throws IOException {
        UserOnlyLogin userOnlyLogin = new UserOnlyLogin();
        userOnlyLogin.setLogin(LoginController.getInstance().getLoginChoose());
        userOnlyLogin.setPassword(LoginController.getInstance().getPasswordChoose());
        userOnlyLogin.setMessageType(MessageType.FIRSTLOGIN);
        oos.writeObject(userOnlyLogin);
    }
    /* This method is used for sending a normal Message
     * @param msg - The message which the user generates
     */

    public static void send(String msg) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.USER);
        createMessage.setStatus(Status.AWAY);
        createMessage.setMsg(msg);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }
    /* This method is used for sending a voice Message
     * @param msg - The message which the user generates
     */
    public static void sendVoiceMessage(byte[] audio) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.VOICE);
        createMessage.setStatus(Status.AWAY);
        createMessage.setVoiceMsg(audio);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

    /* This method is used for sending a normal Message
     * @param msg - The message which the user generates
     */
    public static void sendStatusUpdate(Status status) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.STATUS);
        createMessage.setStatus(status);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

}
