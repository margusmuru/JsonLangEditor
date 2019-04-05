package csvmanagement;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonManageService {

    private static JsonManageService SINGLE_INSTANCE = null;
    private JsonNode jsonNode;

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

    public boolean readJsonFromTextArea(String jsonData) {
        jsonNode = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
            if (!jsonData.isEmpty()) {
                jsonNode = objectMapper.readTree(jsonData);
                System.out.println(jsonNode.toString());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
