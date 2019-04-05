package csvmanagement;

import com.google.common.collect.ImmutableList;
import csvmanagement.models.CsvLine;
import javafx.stage.Stage;

import java.util.List;

public class CsvManageService {
    private Stage window;
    private List<CsvLine> records;

    private static CsvManageService SINGLE_INSTANCE = null;

    private CsvManageService() {
    }

    public static CsvManageService getInstance() {
        if (SINGLE_INSTANCE == null) {
            synchronized (CsvManageService.class) {
                SINGLE_INSTANCE = new CsvManageService();
            }
        }
        return SINGLE_INSTANCE;
    }

    public void setParsedCsvData(List<CsvLine> records) {
        this.records = records;
    }

    public List<CsvLine> getParsedCsvData() {
        return ImmutableList.copyOf(records);
    }

    public void removeRecord(int index) {
        if (records.size() > index) {
            records.remove(index);
        }
    }

    public void removeRecords(List<Integer> indexList) {
        if (indexList == null || indexList.isEmpty()) {
            return;
        }
        int shiftPositions = 0;
        for (Integer element : indexList) {
            int index = element - shiftPositions;
            if (index >= 0 && records.size() > index) {
                records.remove(index);
                shiftPositions++;
            }
        }
    }

}
