package layouts;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static javafx.scene.control.TabPane.TabClosingPolicy.*;

public class TabView {
    private BorderPane mainLayout;
    private Stage window;

    public TabView(Stage window, BorderPane mainLayout) {
        this.mainLayout = mainLayout;
        this.window = window;
        createTabLayout();
    }

    private void createTabLayout() {
        TabPane tabpane = new TabPane();
        tabpane.setTabClosingPolicy(UNAVAILABLE);
        Tab importCsv = new Tab("Import CSV");
        // csv view adds itself
        CsvView csvView = new CsvView(window, importCsv);
        tabpane.getTabs().add(importCsv);
        mainLayout.setCenter(tabpane);
    }


}
