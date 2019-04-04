package mainPackage;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class TopMenuBar {

    private HBox boxLayout;
    private Main mainClass;

    public TopMenuBar(Main mainClass) {
        this.mainClass = mainClass;
        createButtons();
    }

    public HBox getLayout() {
        return boxLayout;
    }

    private void createButtons() {
        boxLayout = new HBox();
        Button btnImport = new Button("Import CSV");
        boxLayout.getChildren().add(btnImport);
    }

    public void importCsv(ActionEvent event) {

    }

}
