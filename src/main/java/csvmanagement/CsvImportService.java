package csvmanagement;

import com.opencsv.CSVReader;
import csvmanagement.models.CsvLine;
import fileaccess.DataReader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainPackage.Main;
import mainPackage.models.MessageType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvImportService {
    private Stage window;
    private DataReader dataReader;

    public CsvImportService(Stage window) {
        this.window = window;
        dataReader = new DataReader();
    }

    public List<CsvLine> getParsedCsvFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        File file = fileChooser.showOpenDialog(window);
        List<CsvLine> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(dataReader.openFile(file))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                records.add(new CsvLine(values));
            }
        } catch (Exception e) {
            Main.setMessage(file.getAbsolutePath() + " " + e.getMessage(), MessageType.ERROR);
        }
        return records;
    }

}
