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
    }

    public static CsvManagerService getInstance() {
        if (SINGLE_INSTANCE == null) {
            synchronized (CsvManagerService.class) {
                SINGLE_INSTANCE = new CsvManagerService();
            }
        }
        return SINGLE_INSTANCE;
    }

    public void setRecords(List<String[]> data) {
        observableRecords.clear();
        observableRecords.addAll(data);
        recordsHistory.add(FXCollections.observableArrayList(observableRecords));
        recordsHistoryLocationIndex++;
        clearRedoHistory();
    }

    public void clearRecords() {
        observableRecords.clear();
        recordsHistory.add(FXCollections.observableArrayList(observableRecords));
        recordsHistoryLocationIndex++;
        clearRedoHistory();
    }

    public List<List<String[]>> getRecordsHistory(){
        return recordsHistory;
    }

    public void clearHistory() {
        recordsHistoryLocationIndex = 0;
        recordsHistory.clear();
    }

    public boolean canUndo() {
        return recordsHistoryLocationIndex > 0;
    }

    public void undoRecords() {
        if (!canUndo()) {
            return;
        }
        actionStep(-1);
    }

    public boolean canRedo() {
        return recordsHistory.size() > recordsHistoryLocationIndex;
    }

    public void redoRecords() {
        if (!canRedo()) {
            return;
        }
        actionStep(1);
    }

    private void actionStep(int index) {
        recordsHistoryLocationIndex = recordsHistoryLocationIndex + index;
        observableRecords.clear();
        if (recordsHistoryLocationIndex > 0) {
            List<String[]> copy = new ArrayList<>();
            recordsHistory.get(recordsHistoryLocationIndex - 1).forEach(element -> copy.add(element.clone()));
            observableRecords.addAll(copy);
        }
    }

    private void clearRedoHistory() {
        if (recordsHistory.size() > recordsHistoryLocationIndex + 1) {
            for (int index = recordsHistoryLocationIndex + 1; index <= recordsHistory.size(); index++) {
                recordsHistory.remove(index);
            }
        }
    }

    public ObservableList<String[]> getObservableRecords() {
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
        recordsHistory.add(FXCollections.observableArrayList(observableRecords));
        recordsHistoryLocationIndex++;
    }


    @Deprecated
    public void setParsedCsvData(List<CsvLine> records) {
        this.records = records;
    }

    @Deprecated
    public List<CsvLine> getParsedCsvData() {
        return ImmutableList.copyOf(records);
    }

    @Deprecated
    public boolean hasParsedCsvData() {
        return records != null && !records.isEmpty();
    }

    @Deprecated
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
