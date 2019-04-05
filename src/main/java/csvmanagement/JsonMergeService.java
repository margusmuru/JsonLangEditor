package csvmanagement;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import csvmanagement.models.CsvLine;
import csvmanagement.models.SelectedCsv;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class JsonMergeService {

    private static JsonMergeService SINGLE_INSTANCE = null;
    private JsonNode jsonNode;
    private CsvManageService csvManageService;

    private JsonMergeService() {
        csvManageService = CsvManageService.getInstance();
    }

    public static JsonMergeService getInstance() {
        if (SINGLE_INSTANCE == null) {
            synchronized (JsonMergeService.class) {
                SINGLE_INSTANCE = new JsonMergeService();
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

    public List<SelectedCsv> getSelectedCsvColumns(int keyCol, int valueCol) {
        if (!csvManageService.hasParsedCsvData()) {
            return emptyList();
        }
        List<CsvLine> csvRecords = csvManageService.getParsedCsvData();
        return csvRecords.stream().map(
                el -> new SelectedCsv(getColumn(el, keyCol), getColumn(el, valueCol))
        ).collect(toList());
    }

    private String getColumn(CsvLine csvLine, int index) {
        switch (index) {
            case 0:
                return csvLine.getKey();
            case 1:
                return csvLine.getEt();
            case 2:
                return csvLine.getRu();
            case 3:
                return csvLine.getEn();
        }
        return null;
    }

}
