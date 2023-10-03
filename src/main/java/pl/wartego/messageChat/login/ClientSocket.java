package pl.wartego.messageChat.login;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocket {
   public static Socket socket = null;
    public static InputStreamReader inputStreamReader = null;
    public static OutputStreamWriter outputStreamWriter = null;
    public static BufferedWriter bufferedWriter = null;
    public static BufferedReader bufferedReader = null;
    public static void newClient() throws IOException {
        try{
            socket = new Socket("localhost",5555);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
//            Scanner scanner = new Scanner(System.in);
        } catch (IOException e){
            e.printStackTrace();
        }
        finally {
            inputStreamReader.close();
            outputStreamWriter.close();
            bufferedWriter.close();
            bufferedReader.close();
            socket.close();
        }

    }
}
