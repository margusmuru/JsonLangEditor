package settings;

import mainPackage.Main;
import mainPackage.models.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class DataWriter {

    public void writeObjectToDisk(Object data, String fileName, String filePath) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + fileName);
            new ObjectOutputStream(fileOutputStream).writeObject(data);
            Main.setMessage(fileName + " write successful!", MessageType.SUCCESS);
        } catch (Exception e) {
            Main.setMessage(fileName + " " + e.getMessage(), MessageType.ERROR);
        }
    }
}
