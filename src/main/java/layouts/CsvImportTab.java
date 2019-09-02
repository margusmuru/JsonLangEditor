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
import javafx.util.Callback;
import mainPackage.Main;
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

    public CsvImportTab() {
        super();
        createTabWithName("Import CSV");
        csvImportService = CsvImportService.getInstance();
        csvManagerService = CsvManagerService.getInstance();

        HBox buttonBar = new HBox();
        buttonBar.getChildren().addAll(createRegion(10), createSeparatorSelector(), createRegion(20));
        buttonBar.getChildren().addAll(createRegion(10), createImportButton(), createRegion(20));
        buttonBar.getChildren().addAll(createClearButton(), createSeparator());
        buttonBar.getChildren().addAll(createUndoButton(), createRegion(10));
        buttonBar.getChildren().addAll(createRedoButton());
        tableView = createTableView();
        mainLayout.getChildren().addAll(buttonBar, tableView);
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

    private Button createImportButton() {
        Button btnImport = new Button();
        Image iconOpen = new Image(getClass().getResourceAsStream("/icons/icon_open.png"));
        ImageView iconOpenView = new ImageView(iconOpen);
        iconOpenView.setFitHeight(buttonHeight);
        iconOpenView.setFitWidth(buttonWidth);
        btnImport.setGraphic(iconOpenView);
        btnImport.setTooltip(new Tooltip("Import a CSV file"));
        btnImport.setOnAction(event -> importCsv());
        return btnImport;
    }

    private Button createClearButton() {
        Button btnClear = new Button();
        Image iconClear = new Image(getClass().getResourceAsStream("/icons/icon_delete.png"));
        ImageView iconOpenView = new ImageView(iconClear);
        iconOpenView.setFitHeight(buttonHeight);
        iconOpenView.setFitWidth(buttonWidth);
        btnClear.setGraphic(iconOpenView);
        btnClear.setTooltip(new Tooltip("Clear CSV"));
        btnClear.setOnAction(event -> clearCsv());
        return btnClear;
    }

    private Button createUndoButton(){
        Button btnUndo = new Button();
        Image iconUndo = new Image(getClass().getResourceAsStream("/icons/icon_undo.png"));
        ImageView iconUndoView = new ImageView(iconUndo);
        iconUndoView.setFitHeight(buttonHeight);
        iconUndoView.setFitWidth(buttonWidth);
        btnUndo.setGraphic(iconUndoView);
        btnUndo.setTooltip(new Tooltip("Undo"));
        btnUndo.setOnAction(event -> undo());
        return btnUndo;
    }

    private Button createRedoButton(){
        Button btnRedo = new Button();
        Image iconUndo = new Image(getClass().getResourceAsStream("/icons/icon_redo.png"));
        ImageView iconRedoView = new ImageView(iconUndo);
        iconRedoView.setFitHeight(buttonHeight);
        iconRedoView.setFitWidth(buttonWidth);
        btnRedo.setGraphic(iconRedoView);
        btnRedo.setTooltip(new Tooltip("Redo"));
        btnRedo.setOnAction(event -> redo());
        return btnRedo;
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

    private void importCsv() {
        List<String[]> csvData = csvImportService.getParsedCsvArray();
        if (csvData.isEmpty()) {
            return;
        }
        setCsvDataToView(csvData);
    }

    private void clearCsv() {
        csvManagerService.clearRecords();
    }

    private void undo() {
        csvManagerService.undoRecords();
    }

    private void redo() {
        csvManagerService.redoRecords();
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
                    element -> indexList.add(element.getRow())
            );
            Collections.sort(indexList);
            csvManagerService.removeRecords(indexList);
        } catch (Exception e) {
            Main.setMessage("Unable to remove lines.", MessageType.ERROR);
        }
    }
}
