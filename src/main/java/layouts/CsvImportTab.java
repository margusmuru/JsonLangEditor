package layouts;

import com.sun.java.swing.plaf.windows.WindowsOptionPaneUI;
import csvmanagement.CsvImportService;
import csvmanagement.CsvManagerService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import mainPackage.Main;
import mainPackage.models.MessageType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvImportTab extends MyTabImpl {

    private CsvImportService csvImportService;
    private CsvManagerService csvManagerService;
    private TableView<String[]> tableView;
    private char csvImportSeparator = ',';

    public CsvImportTab() {
        super();
        createTabWithName("Import CSV");
        csvImportService = CsvImportService.getInstance();
        csvManagerService = CsvManagerService.getInstance();


        this.mainLayout.getChildren().addAll(createImportButton());
        tableView = createTableView();
        mainLayout.getChildren().add(tableView);
    }

    private Button createImportButton() {
        Button btnImport = new Button("Import");
        btnImport.setTooltip(
                new Tooltip("Import a CSV file containing maximum of 4 columns [keys, lang1, lang2, lang3]"));
        btnImport.setOnAction(event -> importCsv());
        return btnImport;
    }

    private HBox createSeparatorSelector() {
        Label keyLabel = new Label("Separator: ");
        ComboBox<Character> keyColumnSelector = new ComboBox<>();
        ObservableList<Character> keyItems = FXCollections.observableArrayList(';', ',');
        keyColumnSelector.setItems(keyItems);
        keyColumnSelector.getSelectionModel().select(1);
        keyColumnSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            csvImportSeparator = newItem;
        });
        return new HBox(keyLabel, keyColumnSelector);
    }

    private void importCsv() {
        List<String[]> csvData = csvImportService.getParsedCsvArray();
        if (csvData.isEmpty()) {
            return;
        }
        // csvManageService.setParsedCsvData(csvData);
        setCsvDataToView(csvData);
    }

    private void setCsvDataToView(List<String[]> input) {
        csvManagerService.addRecords(input);
        for (int i = 0; i < calculateColumnCount(input); i++) {
            TableColumn tableColumn = new TableColumn(input.get(0)[i]);
            final int colNo = i;
            tableColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>)
                    p -> new SimpleStringProperty((p.getValue()[colNo])));
            tableView.getColumns().add(tableColumn);
        }
        tableView.setItems(csvManagerService.getRecords());
    }

    private int calculateColumnCount(List<String[]> input) {
        int colCount = 0;
        for (String[] line : input) {
            if (line.length > colCount) {
                colCount = line.length;
            }
        }
        return colCount;
    }

    private TableView createTableView() {
        TableView<String[]> tableView = new TableView<>();
        Label placeholder = new Label();
        placeholder.setText("Use Import button to add CSV data");
        tableView.setPlaceholder(placeholder);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.prefHeightProperty().bind(Main.getWindow().heightProperty());
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                removeSelectedLines();
            }
        });

        MenuItem mi1 = new MenuItem("Remove lines");
        mi1.setOnAction((ActionEvent event) -> {
            removeSelectedLines();
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        tableView.setContextMenu(menu);

        return tableView;
    }

    private void removeSelectedLines() {
        try {
            List<Integer> indexList = new ArrayList<>();
            tableView.getSelectionModel().getSelectedCells().forEach(
                    element -> indexList.add(((TablePosition) element).getRow())
            );
            Collections.sort(indexList);
            csvManagerService.removeRecords(indexList);
            //setCsvDataToView(csvManageService.getParsedCsvData());
        } catch (Exception e) {
            Main.setMessage("Unable to remove lines.", MessageType.ERROR);
        }
    }
}
