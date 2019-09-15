package csvmanagement;

import com.opencsv.CSVReader;
import csvmanagement.models.CsvLine;
import fileaccess.DataReader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import mainPackage.Main;
import mainPackage.models.MessageType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CsvImportService {
    private Stage window;
    private DataReader dataReader;
    private char columnSeparatorChar = ',';
    private FileChooser fileChooser;
    @Getter
    private String initialPath;

    private static CsvImportService SINGLE_INSTANCE = null;

    private CsvImportService() {
        dataReader = new DataReader();
        this.window = Main.getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
    }

    public void setInitialPath(String path) {
        initialPath = path;
        fileChooser.setInitialDirectory(new File(path));
    }

    public void setColumnSeparatorChar(char value) {
        if (value != ',' && value != ';') {
            throw new UnsupportedOperationException("Invalid separator character");
        }
        columnSeparatorChar = value;
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
        File file = fileChooser.showOpenDialog(window);
        if (file == null) {
            return records;
        }
        String absolutePath = file.getAbsolutePath();
        initialPath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
        fileChooser.setInitialDirectory(new File(initialPath));
        try (CSVReader csvReader = new CSVReader(dataReader.openFile(file), columnSeparatorChar)) {
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
