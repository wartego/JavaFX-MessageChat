package pl.messagechat.messageChat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class ImageController {

    static Logger logger = LoggerFactory.getLogger(SocketController.class);

    public static void sendImagetoServer(File fileToSend, String username){
        if(fileToSend != null){
            try(FileInputStream fileInputStream = new FileInputStream(fileToSend.getAbsolutePath())){

                OutputStream outputStream = SocketController.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                //String fileName = fileToSend.getName();
                //changing File name to username + .png
                username = username + ".png";
                byte[] fileNameBytes = username.getBytes();

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


    public void receivedFileFromServer(InputStream inputStream){
        byte[] fileContentBytes = null;
        try {
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            int fileNameLength = dataInputStream.readInt();

            if(fileNameLength > 0) {
                byte[] fileNameBytes = new byte[fileNameLength];
                dataInputStream.readFully(fileNameBytes,0,fileNameBytes.length);
                String fileName = new String(fileNameBytes);

                boolean fileExist = checkIfFileAlreadyExist(fileName);

                //check if file exist already in folder
                if(!fileExist){
                    int fileContentLength = dataInputStream.readInt();

                    if(fileContentLength > 0){
                        fileContentBytes = new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes,0,fileContentLength);
                    }
                    File fileToDownload = new File("src/main/resources/pictures/userPictures/",fileName);
                    try{
                        FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
                        fileOutputStream.write(fileContentBytes);
                        logger.info("file was successfully saved");
                        //fileOutputStream.close(); probably closing SOCKET
                    } catch (IOException e){
                        logger.info("something wrong during collection file and saving");
                    }
                }
            }
            //dataInputStream.close(); probably closing SOCKET

        } catch (IOException e){
            logger.error("File handler error");
        }
    }

    public static boolean checkIfFileAlreadyExist(String filename){
        File folder = new File("src/main/resources/pictures/userPictures");
        File[] fileList = folder.listFiles();
        if(fileList != null){
            for(File currentFile: fileList){
                if(currentFile.getName().equals(filename)){
                    logger.info(String.format("File %s is exist, will be not added", filename));
                    return true;
                }
                }
            }
        //if file will be not finded
        return false;
        }
    }

