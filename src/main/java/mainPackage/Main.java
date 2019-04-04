package mainPackage;

import csvmanagement.CsvImportService;
import csvmanagement.CsvManageService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import layouts.MessageBar;
import layouts.TopMenuBar;
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
    private static MessageBar messageBar;
    private CsvImportService csvImportService;
    private CsvManageService csvManageService;

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

        csvImportService = new CsvImportService(window);
        csvManageService = new CsvManageService(window);

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
        messageBar.setMessage(message, messageType);
    }

    private void setupUiComponents() {
        mainLayout = new BorderPane();
        setupMessageBox();
        setupTopMenuBar();
    }

    private void setupMessageBox() {
        messageBar = new MessageBar();
        mainLayout.setBottom(messageBar.getLayout());
    }

    private void setupTopMenuBar(){
        mainLayout.setTop(new TopMenuBar(this).getLayout());
    }

    public void importCsv(){
        csvManageService.setParsedCsvData(csvImportService.getParsedCsvFile());
    }

    public void closeProgram() {
        settingsService.saveCurrentSettings();
        window.close();
    }

}
