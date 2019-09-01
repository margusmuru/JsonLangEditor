package layouts;

import csvmanagement.CsvImportService;
import csvmanagement.CsvManagerService;
import csvmanagement.models.CsvLine;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainPackage.Main;
import mainPackage.models.MessageType;
import uielements.ButtonType;
import uielements.MyButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvView {
    private Tab tabLayout;
    private VBox vbox;
    HBox buttonBarLayout;
    private Stage window;
    private CsvImportService csvImportService;
    private CsvManagerService csvManagerService;
    private TableView tableView;

    public CsvView(Stage window, Tab tabLayout) {
        this.window = window;
        this.tabLayout = tabLayout;
        csvImportService = CsvImportService.getInstance();
        csvManagerService = CsvManagerService.getInstance();

        vbox = new VBox();
        buttonBarLayout = new HBox();
        createContainerWithButtons();
        addData(null);
    }

    public void addData(List<CsvLine> data) {
        tableView = new TableView();
        Label placeholder = new Label();
        placeholder.setText("Use Import button to add CSV data");
        tableView.setPlaceholder(placeholder);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<String, CsvLine> colKey = new TableColumn<>("key");
        colKey.setSortable(false);
        colKey.setCellValueFactory(new PropertyValueFactory<>("key"));
        colKey.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        TableColumn<String, CsvLine> etCol = new TableColumn<>("et");
        etCol.setSortable(false);
        etCol.setCellValueFactory(new PropertyValueFactory<>("et"));
        etCol.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        TableColumn<String, CsvLine> ruCol = new TableColumn<>("ru");
        ruCol.setSortable(false);
        ruCol.setCellValueFactory(new PropertyValueFactory<>("ru"));
        ruCol.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        TableColumn<String, CsvLine> enCol = new TableColumn<>("en");
        enCol.setSortable(false);
        enCol.setCellValueFactory(new PropertyValueFactory<>("en"));
        enCol.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        if(data != null){
            data.forEach(csvLine -> {
                tableView.getItems().add(csvLine);
            });
        }
        tableView.getColumns().addAll(colKey, etCol, ruCol, enCol);
        tableView.prefHeightProperty().bind(window.heightProperty());
        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        assignDeleteKeyFunctionality();
        vbox = new VBox(buttonBarLayout, tableView);
        tabLayout.setContent(vbox);
    }

    private void createContainerWithButtons() {
        buttonBarLayout = new HBox();
        buttonBarLayout.getChildren().add(createImportButton());
        Region spring = new Region();
        HBox.setHgrow(spring, Priority.ALWAYS);
        buttonBarLayout.getChildren().add(spring);
        buttonBarLayout.getChildren().add(createRemoveSelectedLineButton());
        buttonBarLayout.setPadding(new Insets(4, 4, 4, 4));
        buttonBarLayout.setSpacing(10);
        vbox = new VBox(buttonBarLayout);
        tabLayout.setContent(vbox);
    }

    private Button createImportButton() {
        MyButton btnImport = new MyButton(ButtonType.ALERT,"Import CSV");
        btnImport.setTooltip(
                new Tooltip("Import a CSV file containing maximum of 4 columns [keys, lang1, lang2, lang3]"));
        btnImport.setOnAction(event -> importCsv());
        return btnImport;
    }

    private void importCsv() {
        csvImportService = CsvImportService.getInstance();
        List<CsvLine> csvData = csvImportService.getParsedCsvLines();
        csvManagerService.setParsedCsvData(csvData);
        addData(csvData);
        tabLayout.setContent(vbox);
    }

    private Button createRemoveSelectedLineButton() {
        Button btnRemove = new Button("Remove selected line");
        btnRemove.setStyle("-fx-background-color: #ff5953; " +
                "-fx-border-color: #828282; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 2 2 2 2;");
        btnRemove.setTooltip(new Tooltip("Removes selected line from CSV view"));
        btnRemove.setOnAction(event -> removeSelectedLines());
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
        try{
            List<Integer> indexList = new ArrayList<>();
            tableView.getSelectionModel().getSelectedCells().forEach(
                    element -> indexList.add(((TablePosition) element).getRow())
            );
            Collections.sort(indexList);
            csvManagerService.removeRecords(indexList);
            addData(csvManagerService.getParsedCsvData());
        } catch (Exception e){
            Main.setMessage("Unable to remove lines.", MessageType.ERROR);
        }
    }

}
