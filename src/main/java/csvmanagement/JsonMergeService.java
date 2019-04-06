package csvmanagement;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import csvmanagement.models.SelectedCsv;
import lombok.Getter;
import mainPackage.Main;
import mainPackage.models.MessageType;

import java.io.IOException;
import java.util.List;

public class JsonMergeService {

    private static JsonMergeService SINGLE_INSTANCE = null;
    @Getter
    private JsonNode jsonNode;
    private ObjectMapper mapper;

    private JsonMergeService() {
        mapper = new ObjectMapper();
        mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
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

    public void add(List<SelectedCsv> selectedCsv) {
        selectedCsv.forEach(el -> ((ObjectNode) jsonNode).put(el.getKeyField(), el.getValueField()));
    }

    public boolean hasElement(SelectedCsv element) {
        return jsonNode.has(element.getKeyField());
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
