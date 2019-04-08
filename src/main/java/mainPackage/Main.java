package mainPackage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import layouts.CsvView;
import layouts.MessageBar;
import layouts.TabView;
import lombok.Getter;
import mainPackage.models.MessageType;
import settings.SettingsService;

public class Main extends Application {

    @Getter
    private static Stage window;
    @Getter
    private static Scene scene;
    private static BorderPane mainLayout;

    private SettingsService settingsService;
    private static MessageBar messageBar;
    private CsvView csvView;
    private TabView tabView;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("JsonLangEditor 0.2");
        // window.getIcons().addElement(new Image("Images/Logo.png"));
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        setupUiComponents();


        settingsService = SettingsService.getInstance();
        settingsService.loadAndApplySettings();
        setMessage(settingsService.getSettings().toString(), MessageType.SUCCESS);

        scene = new Scene(mainLayout,
                settingsService.getSettings().getStageW(),
                settingsService.getSettings().getStageH());
        window.setScene(scene);
        window.show();
    }

    public static void setMessage(String message, MessageType messageType) {
        messageBar.setMessage(message, messageType);
    }

    private void setupUiComponents() {
        mainLayout = new BorderPane();
        setupMessageBox();
        setupTabs();
    }

    private void setupMessageBox() {
        messageBar = new MessageBar();
        mainLayout.setBottom(messageBar.getLayout());
    }

    private void setupTabs() {
        tabView = new TabView(window, mainLayout);
    }

    private void closeProgram() {
        settingsService.saveCurrentSettings();
        window.close();
    }

}
