package csvmanagement;

import javafx.stage.Stage;

import java.util.List;

public class CsvManageService {
    private Stage window;
    private List<List<String>> records;
    private CsvImportService csvImportService;

    public CsvManageService(Stage window) {
        csvImportService = new CsvImportService(window);
    }

    public void setParsedCsvData(List<List<String>> records) {
        this.records = records;
    }

    public void removeNumberOfLines(int numberOfLines) {
        if (records == null) {
            return;
        }
        for (int i = 0; i < numberOfLines; i++) {
            records.remove(0);
        }
    }

}
