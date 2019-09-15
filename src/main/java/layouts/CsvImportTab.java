package layouts;

import csvmanagement.CsvImportService;
import csvmanagement.CsvManagerService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import mainPackage.Main;
import mainPackage.models.ColumnType;
import mainPackage.models.MessageType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvImportTab extends MyTabImpl {
    private final double buttonWidth = 25;
    private final double buttonHeight = 25;
    private CsvImportService csvImportService;
    private CsvManagerService csvManagerService;
    private TableView<String[]> tableView;
    private ObservableList<Integer> columnValueSelectors;
    private List<ComboBox<Integer>> columnValueSelectorComboBoxList = new ArrayList<>();
    private VBox columnValueSelectorsArea;

    public CsvImportTab() {
        super();
        createTabWithName("Import CSV");
        csvImportService = CsvImportService.getInstance();
        csvManagerService = CsvManagerService.getInstance();

        HBox buttonBar = new HBox();
        buttonBar.getChildren().addAll(createRegion(10), createSeparatorSelector(), createRegion(20));
        buttonBar.getChildren().addAll(createRegion(10), createNewButton(), createRegion(20));
        buttonBar.getChildren().addAll(createImportButton(), createRegion(20));
        buttonBar.getChildren().addAll(createClearButton(), createSeparator());
        buttonBar.getChildren().addAll(createUndoButton(), createRegion(10));
        buttonBar.getChildren().addAll(createRedoButton());
        tableView = createTableView();
        columnValueSelectorsArea = createLanguageSelector();
        mainLayout.getChildren().addAll(buttonBar, tableView);
    }

    private Button createNewButton() {
        Button btnNew = createButtonBasic("/icons/icon_new.png", "Create an empty CSV file");
        btnNew.setOnAction(event -> newCsv());
        return btnNew;
    }

    private Button createImportButton() {
        Button btnImport = createButtonBasic("/icons/icon_open.png", "Import a CSV file");
        btnImport.setOnAction(event -> importCsv());
        return btnImport;
    }

    private Button createClearButton() {
        Button btnClear = createButtonBasic("/icons/icon_delete.png", "Clear CSV");
        btnClear.setOnAction(event -> clearCsv());
        return btnClear;
    }

    private Button createUndoButton() {
        Button btnUndo = createButtonBasic("/icons/icon_undo.png", "Undo");
        btnUndo.setOnAction(event -> undo());
        return btnUndo;
    }

    private Button createRedoButton() {
        Button btnRedo = createButtonBasic("/icons/icon_redo.png", "Redo");
        btnRedo.setOnAction(event -> redo());
        return btnRedo;
    }

    private Button createButtonBasic(String iconPath, String toolTip) {
        Button btn = new Button();
        Image icon = new Image(getClass().getResourceAsStream(iconPath));
        ImageView iconView = new ImageView(icon);
        iconView.setFitHeight(buttonHeight);
        iconView.setFitWidth(buttonWidth);
        btn.setGraphic(iconView);
        btn.setTooltip(new Tooltip(toolTip));
        return btn;
    }

    private VBox createLanguageSelector() {
        VBox main = new VBox();
        columnValueSelectors = FXCollections.observableArrayList();
        main.getChildren().addAll(
                createLanguageSelectorRow(ColumnType.KEY),
                createLanguageSelectorRow(ColumnType.ET),
                createLanguageSelectorRow(ColumnType.EN),
                createLanguageSelectorRow(ColumnType.RU)
        );
        return main;
    }

    private HBox createLanguageSelectorRow(ColumnType columnType) {
        HBox hBox = new HBox();
        Label label = new Label(columnType.name() + " is in column ");

        ComboBox<Integer> selector = new ComboBox<>();
        selector.setMinHeight(buttonHeight);
        selector.setMaxHeight(buttonHeight);
        selector.setItems(columnValueSelectors);
        selector.getSelectionModel().select(0);
        selector.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            csvManagerService.setLanguageColumnMapEntry(columnType, newItem);
        });
        columnValueSelectorComboBoxList.add(selector);
        hBox.getChildren().addAll(createRegion(10), label, createRegion(10), selector);
        return hBox;
    }

    private List<Integer> generateRowSelectionNumbers(int count) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(i);
        }
        return list;
    }

    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.setMaxWidth(40);
        separator.setMinWidth(40);
        separator.setOrientation(Orientation.VERTICAL);
        return separator;
    }

    private Region createRegion(double size) {
        Region region = new Region();
        region.setMinWidth(size);
        region.setMaxWidth(size);
        return region;
    }

    private ComboBox<Character> createSeparatorSelector() {
        ComboBox<Character> keyColumnSelector = new ComboBox<>();
        keyColumnSelector.setTooltip(new Tooltip("Set column separator used in your CSV file"));
        keyColumnSelector.setMinHeight(buttonHeight);
        keyColumnSelector.setMaxHeight(buttonHeight);
        ObservableList<Character> keyItems = FXCollections.observableArrayList(',', ';');
        keyColumnSelector.setItems(keyItems);
        keyColumnSelector.getSelectionModel().select(0);
        keyColumnSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            csvImportService.setColumnSeparatorChar(newItem);
        });
        return keyColumnSelector;
    }

    private void newCsv() {
        csvManagerService.clearRecords();
        setCsvDataToView(Collections.singletonList(new String[]{"Key", "ET", "EN", "RU"}));
    }

    private void importCsv() {
        List<String[]> csvData = csvImportService.getParsedCsvArray();
        if (csvData.isEmpty()) {
            return;
        }
        setCsvDataToView(csvData);
    }

    private void refreshColumnValueSelectors() {
        int recordsColumnCount = csvManagerService.getRecordsColumnCount();
        this.columnValueSelectors.clear();
        this.columnValueSelectors.add(null);
        this.columnValueSelectors.addAll(generateRowSelectionNumbers(recordsColumnCount));

        mainLayout.getChildren().remove(columnValueSelectorsArea);
        if (recordsColumnCount > 0) {
            mainLayout.getChildren().add(columnValueSelectorsArea);
            csvManagerService.clearLanguageColumnMap();
            for (int i = 0; i < recordsColumnCount; i++) {
                columnValueSelectorComboBoxList.get(i).getSelectionModel().select(i + 1);
                csvManagerService.setLanguageColumnMapEntry(ColumnType.getByIndex(i), i);
                if (i == 3) {
                    break;
                }
            }
        }
    }

    private void clearCsv() {
        csvManagerService.clearRecords();
        setCsvDataToView(new ArrayList<>());
        refreshColumnValueSelectors();
    }

    private void undo() {
        csvManagerService.undoRecords();
        refreshColumnValueSelectors();
    }

    private void redo() {
        csvManagerService.redoRecords();
        refreshColumnValueSelectors();
    }

    private void setCsvDataToView(List<String[]> input) {
        csvManagerService.setRecords(input);
        tableView.getColumns().clear();
        for (int i = 0; i < calculateColumnCount(input); i++) {
            TableColumn tableColumn = new TableColumn(String.valueOf(i));
            final int colNo = i;
            tableColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>)
                    p -> new SimpleStringProperty((p.getValue()[colNo])));
            tableView.getColumns().add(tableColumn);
        }
        tableView.setItems(csvManagerService.getObservableRecords());
        refreshColumnValueSelectors();
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
                    element -> indexList.add(element.getRow())
            );
            Collections.sort(indexList);
            csvManagerService.removeRecords(indexList);
        } catch (Exception e) {
            Main.setMessage("Unable to remove lines.", MessageType.ERROR);
        }
    }
}
