package layouts;

import csvmanagement.JsonManageService;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainPackage.Main;
import mainPackage.models.MessageType;

public class MergeView {
    private VBox vbox;
    private Stage window;
    private JsonManageService jsonManageService;
    TextArea pasteArea;

    public MergeView(Stage window, Tab tabLayout) {
        this.window = window;
        jsonManageService = JsonManageService.getInstance();
        vbox = new VBox();

        pasteArea = createPasteArea();
        pasteArea.textProperty().addListener((observable, oldValue, newValue) -> {
            parseDataAndAddData();
        });
        vbox.getChildren().add(pasteArea);

        tabLayout.setContent(vbox);
    }

    private TextArea createPasteArea() {
        TextArea pasteArea = new TextArea();
        pasteArea.prefHeightProperty().bind(window.heightProperty());
        return pasteArea;
    }

    private void parseDataAndAddData() {
        boolean isJsonValid = jsonManageService.readJsonFromTextArea(pasteArea.getText());
        if (isJsonValid) {
            pasteArea.setStyle("-fx-background-color: #fffaf5");
            Main.setMessage(null, MessageType.SUCCESS);
        } else {
            pasteArea.setStyle("-fx-text-fill: #ff0005");
            Main.setMessage("Invalid json", MessageType.ERROR);
        }
    }


}
