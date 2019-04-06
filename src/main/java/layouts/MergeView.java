package layouts;

import com.google.common.collect.ImmutableList;
import csvmanagement.CsvManageService;
import csvmanagement.JsonMergeService;
import csvmanagement.models.SelectedCsv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainPackage.Main;
import mainPackage.models.MessageType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MergeView {
    private HBox mainContainer;
    private Stage window;
    private JsonMergeService jsonMergeService;
    private CsvManageService csvManageService;
    private TextArea pasteArea;
    private TableView tableView;
    private List<SelectedCsv> currentCsvData;

    private VBox csvViewContainer;
    private int selectedKeyColumn = 0;
    private int selectedValueColumn = 1;
    private int lastSelectedTableRowIndex = 0;

    public MergeView(Stage window, Tab tabLayout) {
        this.window = window;
        pasteArea = new TextArea();
        jsonMergeService = JsonMergeService.getInstance();
        csvManageService = CsvManageService.getInstance();

        mainContainer = new HBox();

        createJsonViewArea();

        createMergeButtonContainer();

        createCsvContainer(null);


        tabLayout.setContent(mainContainer);
    }

    public void onTabSelected() {
        createCsvContainer(csvManageService.getSelectedCsvColumns(selectedKeyColumn, selectedValueColumn));
    }

    private void createMergeButtonContainer() {
        VBox mergeButtonsContainer = new VBox();
        mergeButtonsContainer.setPadding(new Insets(4, 4, 4, 4));
        mergeButtonsContainer.setSpacing(10);
        Region topSpring = new Region();
        VBox.setVgrow(topSpring, Priority.ALWAYS);
        Region bottomSpring = new Region();
        VBox.setVgrow(bottomSpring, Priority.ALWAYS);

        Button autoMerge = getAutoMergeButton();

        Button mergeSelected = getMergeSelectedButton();

        mergeButtonsContainer.getChildren().addAll(topSpring, autoMerge, mergeSelected, bottomSpring);
        mainContainer.getChildren().add(mergeButtonsContainer);
    }

    @SuppressWarnings("Duplicates")
    private void createCsvContainer(List<SelectedCsv> data) {
        VBox csvViewContainer = new VBox();

        HBox buttonsArea = getCsvButtonsContainer();

        tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<String, SelectedCsv> colKey = new TableColumn<>("Key");
        colKey.setCellValueFactory(new PropertyValueFactory<>("keyField"));
        colKey.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        TableColumn<String, SelectedCsv> etCol = new TableColumn<>("Value");
        etCol.setCellValueFactory(new PropertyValueFactory<>("valueField"));
        etCol.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        if (data != null) {
            currentCsvData = data;
            data.forEach(csvLine -> {
                tableView.getItems().add(csvLine);
            });
        }

        tableView.getColumns().addAll(colKey, etCol);
        tableView.getSelectionModel().select(lastSelectedTableRowIndex);
        tableView.prefHeightProperty().bind(window.heightProperty());
        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        csvViewContainer.getChildren().addAll(buttonsArea, tableView);
        csvViewContainer.prefWidthProperty().bind(window.widthProperty().divide(2));
        mainContainer.getChildren().remove(this.csvViewContainer);
        mainContainer.getChildren().add(csvViewContainer);
        this.csvViewContainer = csvViewContainer;
    }

    @SuppressWarnings("Duplicates")
    private HBox getCsvButtonsContainer() {
        HBox buttonsArea = new HBox();
        buttonsArea.setPadding(new Insets(4, 4, 4, 4));
        buttonsArea.setSpacing(10);

        Label keyLabel = new Label("Keys:");
        ComboBox<Integer> keyColumnSelector = new ComboBox<>();
        ObservableList<Integer> keyItems = FXCollections.observableArrayList(1, 2, 3, 4);
        keyColumnSelector.setItems(keyItems);
        keyColumnSelector.getSelectionModel().select(selectedKeyColumn);
        keyColumnSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            selectedKeyColumn = newItem - 1;
            createCsvContainer(csvManageService.getSelectedCsvColumns(selectedKeyColumn, selectedValueColumn));
        });

        Label valueLabel = new Label("Values:");
        ComboBox<Integer> valueColumnSelector = new ComboBox<>();
        ObservableList<Integer> valueItems = FXCollections.observableArrayList(1, 2, 3, 4);
        valueColumnSelector.setItems(valueItems);
        valueColumnSelector.getSelectionModel().select(selectedValueColumn);
        valueColumnSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            selectedValueColumn = newItem - 1;
            createCsvContainer(csvManageService.getSelectedCsvColumns(selectedKeyColumn, selectedValueColumn));
        });

        buttonsArea.getChildren().addAll(keyLabel, keyColumnSelector, valueLabel, valueColumnSelector);
        return buttonsArea;
    }

    private Button getMergeSelectedButton() {
        Button mergeSelected = new Button("<=");
        mergeSelected.setMinWidth(50);
        mergeSelected.setStyle("-fx-background-color: #89ff87; " +
                "-fx-border-color: #828282; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 2 2 2 2;");
        mergeSelected.setTooltip(
                new Tooltip("Merge selected line from CSV. Merged line will be removed from CSV view"));
        mergeSelected.setOnAction(event -> mergeSelected());
        return mergeSelected;
    }

    private Button getAutoMergeButton() {
        Button autoMerge = new Button("<<=");
        autoMerge.setMinWidth(50);
        autoMerge.setStyle("-fx-background-color: #18ff00; " +
                "-fx-border-color: #828282; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 2 2 2 2;");
        autoMerge.setTooltip(
                new Tooltip("Automerge all translations. Duplicate keys will be removed, respects new values"));
        autoMerge.setOnAction(event -> autoMerge());
        return autoMerge;
    }

    private void autoMerge() {
        lastSelectedTableRowIndex = 0;
        mergeElements(currentCsvData);
    }

    private void mergeSelected() {
        List<Integer> indexes = (List<Integer>) tableView.getSelectionModel().getSelectedCells()
                .stream()
                .map(element -> ((TablePosition) element).getRow())
                .collect(Collectors.toList());
        Collections.sort(indexes);
        lastSelectedTableRowIndex = indexes.size() != 0 ? indexes.get(0) : 0;
        List<SelectedCsv> filteredList = getSubListOfSelectedLines(indexes).stream()
                .filter(element -> !element.getKeyField().isEmpty())
                .collect(Collectors.toList());
        mergeElements(filteredList);
    }

    private void mergeElements(List<SelectedCsv> csvList) {
        int addedElementCount = 0;
        int removedElementCount = 0;
        List<SelectedCsv> listCopy = ImmutableList.copyOf(csvList);
        try {
            for (SelectedCsv selectedCsv : listCopy) {
                if(!selectedCsv.getKeyField().isEmpty()){
                    if (jsonMergeService.hasKey(selectedCsv.getKeyField())) {
                        jsonMergeService.removeElement(selectedCsv);
                        removedElementCount++;
                    }
                    jsonMergeService.addElement(selectedCsv);
                    currentCsvData.remove(selectedCsv);
                    addedElementCount++;
                }
            }
        } catch (Exception e) {
            System.out.println("Error merging");
        }
        createCsvContainer(currentCsvData);
        Main.setMessage("Added " + (addedElementCount - removedElementCount) +
                " keys, replaced " + removedElementCount + " elements", MessageType.SUCCESS);
        pasteArea.setText(jsonMergeService.getPrettyPrintJsonString());
    }

    private List<SelectedCsv> getSubListOfSelectedLines(List<Integer> indexes) {
        List<SelectedCsv> csvElements = new ArrayList<>();
        try {
            indexes.forEach(el -> csvElements.add(currentCsvData.get(el)));
        } catch (IndexOutOfBoundsException e) {
            Main.setMessage("Selected CSV line index is out of bounds", MessageType.ERROR);
        }
        return csvElements;
    }

    private void createJsonViewArea() {
        pasteArea = new TextArea();
        pasteArea.prefWidthProperty().bind(window.widthProperty().divide(2));
        pasteArea.prefHeightProperty().bind(window.heightProperty());
        pasteArea.textProperty().addListener((observable, oldValue, newValue) -> {
            parseAndAddJsonData(newValue);
        });
        mainContainer.getChildren().add(pasteArea);
    }

    private void parseAndAddJsonData(String textValue) {
        boolean isJsonValid = jsonMergeService.setJsonNode(textValue);
        if (isJsonValid) {
            pasteArea.setStyle("-fx-background-color: #fffaf5");
        } else {
            pasteArea.setStyle("-fx-text-fill: #ff0005");
        }
    }


}
