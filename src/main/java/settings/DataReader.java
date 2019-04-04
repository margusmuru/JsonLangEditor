package settings;

import mainPackage.Main;
import mainPackage.models.MessageType;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

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
}
