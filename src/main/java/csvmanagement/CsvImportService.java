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
import java.util.List;

public class CsvImportService {
    private Stage window;
    private DataReader dataReader;

    private static CsvImportService SINGLE_INSTANCE = null;

    private CsvImportService() {
        dataReader = new DataReader();
        this.window = Main.getWindow();
    }

    public static CsvImportService getInstance() {
        if (SINGLE_INSTANCE == null) {
            synchronized (CsvImportService.class) {
                SINGLE_INSTANCE = new CsvImportService();
            }
        }
        return SINGLE_INSTANCE;
    }

    public List<CsvLine> getParsedCsvLines() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        File file = fileChooser.showOpenDialog(window);
        if (file == null) {
            return null;
        }
        List<CsvLine> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(dataReader.openFile(file))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                records.add(new CsvLine(values));
            }
        } catch (Exception e) {
            Main.setMessage(file.getAbsolutePath() + " " + e.getMessage(), MessageType.ERROR);
        }
        Main.setMessage("Imported " + records.size() + " lines of data", MessageType.SUCCESS);
        return records;
    }

    public List<String[]> getParsedCsvArray() {
        List<String[]> records = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        File file = fileChooser.showOpenDialog(window);
        if (file == null) {
            return records;
        }
        try (CSVReader csvReader = new CSVReader(dataReader.openFile(file))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                records.add(values);
            }
        } catch (Exception e) {
            Main.setMessage(file.getAbsolutePath() + " " + e.getMessage(), MessageType.ERROR);
        }
        Main.setMessage("Imported " + records.size() + " lines of data", MessageType.SUCCESS);
        return records;
    }

}
