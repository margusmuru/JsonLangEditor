package layouts;

import csvmanagement.CsvImportService;
import csvmanagement.CsvManageService;
import csvmanagement.models.CsvLine;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvView {
    private BorderPane mainLayout;
    private VBox vbox;
    HBox buttonBarLayout;
    private Stage window;
    private CsvImportService csvImportService;
    private CsvManageService csvManageService;
    private TableView tableView;

    public CsvView(Stage window, BorderPane mainLayout) {
        this.window = window;
        this.mainLayout = mainLayout;
        csvImportService = new CsvImportService(window);
        csvManageService = CsvManageService.getInstance();

        vbox = new VBox();
        buttonBarLayout = new HBox();
        createContainerWithButtons();
    }

    public void addData(List<CsvLine> data) {
        tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<String, CsvLine> colKey = new TableColumn<>("key");
        colKey.setCellValueFactory(new PropertyValueFactory<>("key"));
        colKey.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        TableColumn<String, CsvLine> etCol = new TableColumn<>("et");
        etCol.setCellValueFactory(new PropertyValueFactory<>("et"));
        etCol.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        TableColumn<String, CsvLine> ruCol = new TableColumn<>("ru");
        ruCol.setCellValueFactory(new PropertyValueFactory<>("ru"));
        ruCol.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        TableColumn<String, CsvLine> enCol = new TableColumn<>("en");
        enCol.setCellValueFactory(new PropertyValueFactory<>("en"));
        enCol.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        data.forEach(csvLine -> {
            tableView.getItems().add(csvLine);
        });
        tableView.getColumns().addAll(colKey, etCol, ruCol, enCol);
        tableView.prefHeightProperty().bind(window.heightProperty());
        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        assignDeleteKeyFunctionality();
        vbox = new VBox(buttonBarLayout, tableView);
        mainLayout.setCenter(vbox);
    }

    private void createContainerWithButtons() {
        buttonBarLayout = new HBox();
        buttonBarLayout.getChildren().add(createImportButton());
        buttonBarLayout.getChildren().add(createRemoveSelectedLineButton());
        vbox = new VBox(buttonBarLayout);
        mainLayout.setCenter(vbox);
    }

    private Button createImportButton() {
        Button btnImport = new Button("Import CSV");
        btnImport.setTooltip(
                new Tooltip("Import a CSV file containing maximum of 4 columns [keys, lang1, lang2, lang3]"));
        btnImport.setOnAction(event -> {
            csvImportService = new CsvImportService(window);
            List<CsvLine> csvData = csvImportService.getParsedCsvFile();
            csvManageService.setParsedCsvData(csvData);
            addData(csvData);
            mainLayout.setCenter(vbox);
        });
        return btnImport;
    }

    private Button createRemoveSelectedLineButton() {
        Button btnRemove = new Button("Remove selected line");
        btnRemove.setTooltip(new Tooltip("Removes selected line fron the CSV file"));
        btnRemove.setOnAction(event -> {
            removeSelectedLines();
        });
        return btnRemove;
    }

    private void assignDeleteKeyFunctionality() {
        tableView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                removeSelectedLines();
            }
        });
    }

    private void removeSelectedLines() {
        List<Integer> indexList = new ArrayList<>();
        tableView.getSelectionModel().getSelectedCells().forEach(
                element -> indexList.add(((TablePosition) element).getRow())
        );
        Collections.sort(indexList);
        csvManageService.removeRecords(indexList);
        addData(csvManageService.getParsedCsvData());
    }

}
