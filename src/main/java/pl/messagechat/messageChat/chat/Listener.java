package pl.messagechat.messageChat.chat;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.messagechat.messageChat.login.LoginController;
import pl.messagechat.messageChat.messages.Message;
import pl.messagechat.messageChat.messages.MessageType;
import pl.messagechat.messageChat.messages.Status;
import pl.messagechat.messageChat.messages.UserOnlyLogin;
import pl.messagechat.messageChat.utils.ImageController;
import pl.messagechat.messageChat.utils.SocketController;

import java.io.*;
import java.net.Socket;

public class Listener implements Runnable {
    private static final String HASCONNECTED = "has connected";
    public static String username;
    private static String picture;
    private static ObjectOutputStream oos;
    public String hostname;
    public int port;
    public ChatController controller;
    Logger logger = LoggerFactory.getLogger(Listener.class);
    private Socket socket;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;

    public Listener(String hostname, int port, String username, String picture, ChatController controller) {
        this.hostname = hostname;
        this.port = port;
        Listener.username = username;
        Listener.picture = picture;
        this.controller = controller;

    }

    @Override
    public void run() {

         //creating SocketConnection
            boolean socketConnectionSuccess = SocketController.createSocketConnection();
            if(socketConnectionSuccess){
                socket = SocketController.getSocket();
                LoginController.getInstance().showScene();
                outputStream = SocketController.getOutputStream();
                oos = SocketController.getOos();
                is = SocketController.getIs();
                input = SocketController.getInput();
                logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
            } else{
                logger.error("Could not Connect IO Exception");
                //show error dialog
                LoginController.getInstance().showErrorDialog("Warning"
                        ,"Could not connect to server"
                        ,Alert.AlertType.WARNING
                        ,"Please check for firewall issues and check if the server is running."
                );
            }

        //further working of program
        try {
            connect();
            logger.info("Socket in and out ready");
            while (socket.isConnected()) {
                Message message = null;
                Object dataFromServer = input.readObject();
                if(dataFromServer instanceof Message){
                    message = (Message) dataFromServer;
                    if (message != null) {
                        logger.debug("Message recieved:" + message.getMessageBody() + " MessageType:" + message.getType() + "Name:" + message.getSender());
                        switch (message.getType()) {
                            case USER, VOICE:
                                controller.addToChat(message);
                                break;
                            case NOTIFICATION:
                                controller.newUserNotification(message);
                                //ImageController.receivedFileFromServer(is);
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
                } else {
                    //ImageController.receivedFileFromServer(is);
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            controller.logoutScene(new ActionEvent());
            e.printStackTrace();
        }
    }

    private static void connect() throws IOException {
        Message createMessage = new Message();
        createMessage.setSender(username);
        createMessage.setType(MessageType.CONNECTED);
        createMessage.setMessageBody(HASCONNECTED);
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

    public static void send(String msg) throws IOException {
        Message createMessage = new Message();
        createMessage.setSender(username);
        createMessage.setReceiver("ALL");
        createMessage.setType(MessageType.USER);
        createMessage.setStatus(Status.AWAY);
        createMessage.setMessageBody(msg);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }
    /* This method is used for sending a normal Message
     * @param msg - The message which the user generates
     */

    /* This method is used for sending a voice Message
     * @param msg - The message which the user generates
     */
    public static void sendVoiceMessage(byte[] audio) throws IOException {
        Message createMessage = new Message();
        createMessage.setSender(username);
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
        createMessage.setSender(username);
        createMessage.setType(MessageType.STATUS);
        createMessage.setStatus(status);
        createMessage.setMessageBody("changed to: " + status);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }



}
