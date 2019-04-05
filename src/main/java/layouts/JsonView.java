package layouts;

import com.fasterxml.jackson.databind.JsonNode;
import csvmanagement.JsonManageService;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JsonView {
    private VBox vbox;
    private Stage window;
    private JsonManageService jsonManageService;
    TextArea pasteArea;

    public JsonView(Stage window, Tab tabLayout) {
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
        JsonNode parsedJsonData = jsonManageService.parseJsonData(pasteArea);

    }


}
