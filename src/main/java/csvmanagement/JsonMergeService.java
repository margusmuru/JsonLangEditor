package csvmanagement;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import csvmanagement.models.SelectedCsv;
import lombok.Getter;
import mainPackage.Main;
import mainPackage.models.MessageType;

import java.io.IOException;

public class JsonMergeService {

    private static JsonMergeService SINGLE_INSTANCE = null;
    @Getter
    private JsonNode jsonNode;
    private ObjectMapper mapper;

    private JsonMergeService() {
        mapper = new ObjectMapper();
        mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        jsonNode = mapper.createObjectNode();
    }

    public static JsonMergeService getInstance() {
        if (SINGLE_INSTANCE == null) {
            synchronized (JsonMergeService.class) {
                SINGLE_INSTANCE = new JsonMergeService();
            }
        }
        return SINGLE_INSTANCE;
    }

    public void addElement(SelectedCsv el) {
        ((ObjectNode) jsonNode).put(el.getKeyField(), el.getValueField());
    }

    public void removeElement(SelectedCsv selectedCsv) {
        if (jsonNode.has(selectedCsv.getKeyField())) {
            ((ObjectNode) jsonNode).remove(selectedCsv.getKeyField());
        }
    }

    public boolean hasKey(String key) {
        return jsonNode.has(key);
    }

    public String getPrettyPrintJsonString() {
        try {
            Object json = mapper.readValue(jsonNode.toString(), Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Exception e) {
            Main.setMessage("prettyPrintJsonString error occurred", MessageType.ERROR);
            return null;
        }
    }

    public boolean setJsonNode(String jsonData) {
        try {
            if (jsonData != null && !jsonData.isEmpty()) {
                jsonNode = mapper.readTree(jsonData);
                System.out.println(jsonNode.toString());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
