package pl.messagechat.messageChat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class ImageSendingController {

    static Logger logger = LoggerFactory.getLogger(SocketController.class);

    public static void sendImagetoServer(File fileToSend){
        if(fileToSend != null){
            try(FileInputStream fileInputStream = new FileInputStream(fileToSend.getAbsolutePath())){

                OutputStream outputStream = SocketController.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                String fileName = fileToSend.getName();
                byte[] fileNameBytes = fileName.getBytes();

                byte[] fileContentBytes = new byte[(int) fileToSend.length()];
                fileInputStream.read(fileContentBytes);

                dataOutputStream.writeInt(fileNameBytes.length);
                dataOutputStream.write(fileNameBytes);

                dataOutputStream.writeInt(fileContentBytes.length);
                dataOutputStream.write(fileContentBytes);
                //dataOutputStream.close();
            } catch (IOException e){
                logger.error("Something goes wrong during sending file to server!");
            }
        } else {
            logger.error("file is null");
        }


    }

}
