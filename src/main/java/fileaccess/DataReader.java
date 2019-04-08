package fileaccess;

import mainPackage.Main;
import mainPackage.models.MessageType;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DataReader {

    public Object readObjectFromDisk(String fileName, String filePath) {
        Object obj = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath + fileName);
            obj = new ObjectInputStream(fileInputStream).readObject();
            Main.setMessage(fileName + " successful!", MessageType.SUCCESS);
        } catch (Exception e) {
            Main.setMessage(fileName + " " + e.getMessage(), MessageType.ERROR);
        }
        return obj;
    }

    public Reader openFile(File file) throws Exception{
        return new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file.getAbsolutePath()), StandardCharsets.UTF_8));
    }
}
