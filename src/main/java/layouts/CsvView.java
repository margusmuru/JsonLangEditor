package layouts;

import csvmanagement.models.CsvLine;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

public class CsvView {
    private VBox vbox;

    public CsvView() {
    }

    public void addData(List<CsvLine> data) {
        TableView tableView = new TableView();

        TableColumn<String, CsvLine> colKey = new TableColumn<>("key");
        colKey.setCellValueFactory(new PropertyValueFactory<>("key"));

        TableColumn<String, CsvLine> etCol = new TableColumn<>("et");
        etCol.setCellValueFactory(new PropertyValueFactory<>("et"));

        TableColumn<String, CsvLine> ruCol = new TableColumn<>("ru");
        ruCol.setCellValueFactory(new PropertyValueFactory<>("ru"));

        TableColumn<String, CsvLine> enCol = new TableColumn<>("en");
        enCol.setCellValueFactory(new PropertyValueFactory<>("en"));

        data.forEach(csvLine -> {
            tableView.getItems().add(csvLine);
            System.out.println(csvLine.toString());
        });
        tableView.getColumns().addAll(colKey, etCol, ruCol, enCol);
        vbox = new VBox(tableView);
    }

    public void createEmptyTable() {
        vbox = new VBox();
    }

    public VBox getLayout() {
        return vbox;
    }
}
