package mainPackage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;
import layouts.CsvImportTab;
import layouts.MessageBar;
import layouts.TabLayout;
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

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("JsonLangEditor 0.5-SNAPSHOT");
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
        //new JMetro(JMetro.Style.DARK).applyTheme(scene);
        //scene.getStylesheets().add("/styles.css");
        window.setScene(scene);
        window.show();
    }

    public static void setMessage(String message, MessageType messageType) {
        messageBar.setMessage(message, messageType);
    }

    private void setupUiComponents() {
        mainLayout = new BorderPane();
        setupMessageBox();
        setupTabLayout();
    }

    private void setupMessageBox() {
        messageBar = new MessageBar();
        mainLayout.setBottom(messageBar.getLayout());
    }

    private void setupTabLayout() {
        TabLayout tabLayout = new TabLayout();
        CsvImportTab csvImportTab = new CsvImportTab();
        tabLayout.addTab(csvImportTab.getLayout());
        tabLayout.addTab(new Tab("Something"));
        mainLayout.setCenter(tabLayout.getLayout());
    }

    private void closeProgram() {
        settingsService.saveCurrentSettings();
        window.close();
    }

}
