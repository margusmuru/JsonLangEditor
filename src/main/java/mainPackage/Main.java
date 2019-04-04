package mainPackage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import mainPackage.models.MessageType;
import settings.SettingsService;

public class Main extends Application {

    @Getter
    private static Stage window;
    @Getter
    private static Scene scene;
    private static Label messageBox;
    private static BorderPane mainLayout;

    private SettingsService settingsService;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("JsonLangEditor 0.1");
        // window.getIcons().add(new Image("Images/Logo.png"));
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        setupUiComponents();

        settingsService = new SettingsService();
        settingsService.loadAndApplySettings();
        setMessage(settingsService.getSettings().toString(), MessageType.SUCCESS);

        scene = new Scene(mainLayout,
                settingsService.getSettings().getStageW(),
                settingsService.getSettings().getStageH());
        window.setScene(scene);
        window.show();
    }

    public static void setMessage(String message, MessageType messageType) {
        messageBox.setText(message);
        switch (messageType) {
            case DEFAULT:
                messageBox.setStyle("-fx-text-fill: black");
                break;
            case SUCCESS:
                messageBox.setStyle("-fx-text-fill: green");
                break;
            case ERROR:
                messageBox.setStyle("-fx-text-fill: red");
                break;
        }
    }

    private void setupUiComponents() {
        mainLayout = new BorderPane();
        setupMessageBox();
    }

    private void setupMessageBox() {
        final HBox messageBoxLayout = new HBox();
        messageBox = new Label("");
        messageBoxLayout.getChildren().add(messageBox);
        mainLayout.setBottom(messageBoxLayout);
    }

    private void closeProgram() {
        settingsService.saveCurrentSettings();
        window.close();
    }

}
