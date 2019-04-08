package csvmanagement;

import com.google.common.collect.ImmutableList;
import csvmanagement.models.CsvLine;
import csvmanagement.models.SelectedCsv;
import mainPackage.Main;
import mainPackage.models.MessageType;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class CsvManageService {
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

    public boolean hasParsedCsvData(){
        return  records != null && !records.isEmpty();
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
        Main.setMessage("Removed " + shiftPositions + " lines of data", MessageType.SUCCESS);
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
