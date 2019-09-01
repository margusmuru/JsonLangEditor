package csvmanagement;

import com.google.common.collect.ImmutableList;
import csvmanagement.models.CsvLine;
import csvmanagement.models.SelectedCsv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainPackage.Main;
import mainPackage.models.MessageType;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class CsvManagerService {
    private List<CsvLine> records;
    private ObservableList<String[]> observableRecords = FXCollections.observableArrayList();

    private static CsvManagerService SINGLE_INSTANCE = null;

    private CsvManagerService() {
    }

    public static CsvManagerService getInstance() {
        if (SINGLE_INSTANCE == null) {
            synchronized (CsvManagerService.class) {
                SINGLE_INSTANCE = new CsvManagerService();
            }
        }
        return SINGLE_INSTANCE;
    }

    public void addRecords(List<String[]> data) {
        observableRecords.addAll(data);
    }

    public void clearRecords(){
        observableRecords.clear();
    }

    public ObservableList<String[]> getRecords() {
        return observableRecords;
    }

    public void removeRecords(List<Integer> indexList) {
        if (indexList == null || indexList.isEmpty()) {
            return;
        }
        int shiftPositions = 0;
        for (Integer element : indexList) {
            int index = element - shiftPositions;
            if (index >= 0 && observableRecords.size() > index) {
                observableRecords.remove(index);
                shiftPositions++;
            }
        }
        Main.setMessage("Removed " + shiftPositions + " lines of data", MessageType.SUCCESS);
    }








    public void setParsedCsvData(List<CsvLine> records) {
        this.records = records;
    }

    public List<CsvLine> getParsedCsvData() {
        return ImmutableList.copyOf(records);
    }

    public boolean hasParsedCsvData(){
        return  records != null && !records.isEmpty();
    }



    public List<SelectedCsv> getSelectedCsvColumns(int keyCol, int valueCol) {
        if (!hasParsedCsvData()) {
            return emptyList();
        }
        List<CsvLine> csvRecords = getParsedCsvData();
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
