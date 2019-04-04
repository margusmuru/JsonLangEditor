package csvmanagement;

import csvmanagement.models.CsvLine;
import javafx.stage.Stage;

import java.util.List;

public class CsvManageService {
    private Stage window;
    private List<CsvLine> records;

    public CsvManageService(Stage window) {
    }

    public void setParsedCsvData(List<CsvLine> records) {
        this.records = records;
    }

    public List<CsvLine> getParsedCsvData(){
        return records;
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
