package csvmanagement;

import com.google.common.collect.ImmutableList;
import csvmanagement.models.CsvLine;
import csvmanagement.models.SelectedCsv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainPackage.Main;
import mainPackage.models.MessageType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class CsvManagerService {
    // TODO remove
    private List<CsvLine> records;
    private List<List<String[]>> recordsHistory = new ArrayList<>();
    private int recordsHistoryLocationIndex = 0;
    private ObservableList<String[]> observableRecords = FXCollections.observableArrayList();

    private static CsvManagerService SINGLE_INSTANCE = null;

    private CsvManagerService() {
        // by default history contains empty list;
        recordsHistory.add(new ArrayList<>());
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
        recordsHistory.add(data);
        recordsHistoryLocationIndex++;
        clearRedoHistory();
    }

    public void clearRecords() {
        observableRecords.clear();
        recordsHistory.add(new ArrayList<>());
        recordsHistoryLocationIndex++;
        clearRedoHistory();
    }

    public void undoRecords() {
        recordsHistoryLocationIndex--;
        List<String[]> copy = new ArrayList<>();
        Collections.copy(copy, recordsHistory.get(recordsHistoryLocationIndex));
        observableRecords.clear();
        observableRecords.addAll(copy);
    }

    public void redoRecords() {
        recordsHistoryLocationIndex++;
        List<String[]> copy = new ArrayList<>();
        Collections.copy(copy, recordsHistory.get(recordsHistoryLocationIndex));
        observableRecords.clear();
        observableRecords.addAll(copy);
    }

    private void clearRedoHistory() {
        if (recordsHistory.size() > recordsHistoryLocationIndex + 1) {
            for (int index = recordsHistoryLocationIndex + 1; index <= recordsHistory.size(); index++) {
                recordsHistory.remove(index);
            }
        }
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
        recordsHistory.add(observableRecords);
        recordsHistoryLocationIndex++;
        Main.setMessage("Removed " + shiftPositions + " lines of data", MessageType.SUCCESS);
    }


    public void setParsedCsvData(List<CsvLine> records) {
        this.records = records;
    }

    public List<CsvLine> getParsedCsvData() {
        return ImmutableList.copyOf(records);
    }

    public boolean hasParsedCsvData() {
        return records != null && !records.isEmpty();
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
