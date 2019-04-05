package csvmanagement;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.TextArea;
import mainPackage.Main;
import mainPackage.models.MessageType;

import java.io.IOException;

public class JsonManageService {

    private static JsonManageService SINGLE_INSTANCE = null;

    private JsonManageService() {
    }

    public static JsonManageService getInstance() {
        if (SINGLE_INSTANCE == null) {
            synchronized (JsonManageService.class) {
                SINGLE_INSTANCE = new JsonManageService();
            }
        }
        return SINGLE_INSTANCE;
    }

    public JsonNode parseJsonData(TextArea pasteArea){
        JsonNode jsonNode = null;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
            if(!pasteArea.getText().isEmpty()){
                jsonNode = objectMapper.readTree(pasteArea.getText());
                System.out.println(jsonNode.toString());
            }
            pasteArea.setStyle("-fx-background-color: #fffaf5");
            Main.setMessage(null, MessageType.SUCCESS);
        } catch (IOException e){
            pasteArea.setStyle("-fx-text-fill: #ff0005");
            Main.setMessage("Invalid json", MessageType.ERROR);
        }
        return jsonNode;
    }

}
