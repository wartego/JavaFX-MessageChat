package pl.wartego.messagelink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EmailBody {
    private static final String EMAILBODY = "/emailbody.txt";
    private static List<String> emailbodyTextList;

    protected static void getFileFromResourceAsStream() throws IOException {
        ClassLoader classLoader = EmailBody.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("emailbody.txt");
        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + EMAILBODY);
        }
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        emailbodyTextList = new ArrayList<>();
        System.out.println(reader.readLine());
        while ((line = reader.readLine()) != null) {
            emailbodyTextList.add(line);
        }
    }

    protected static String replaceKeyWordInBodyEmail(String firstName, String verificationCode) {
        StringBuilder stringBuilder = new StringBuilder();
        String newLine;
        emailbodyTextList.forEach(line -> {
            if (line.contains("FIRSTNAME")) {
                emailbodyTextList.set(emailbodyTextList.indexOf(line), line.replace("FIRSTNAME", firstName));
            }
            if (line.contains("CODEXX")) {
                emailbodyTextList.set(emailbodyTextList.indexOf(line), line.replace("CODEXX", verificationCode));
            }
        });
        emailbodyTextList.forEach(line -> stringBuilder.append(line + "\n"));
        return stringBuilder.toString();
    }
}
