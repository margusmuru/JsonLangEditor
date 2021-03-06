package layouts;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;

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

        tabpane.getTabs().addAll(
                createCsvTab(),
                createJsonTab(tabpane));
        tabpane.setTabMinWidth(200);
        mainLayout.setCenter(tabpane);
    }

    private Tab createCsvTab() {
        Tab csvTab = new Tab("Import CSV");
        // csv view adds itself
        CsvView csvView = new CsvView(window, csvTab);
        return csvTab;
    }

    private Tab createJsonTab(TabPane tabPane) {
        Tab jsonTab = new Tab("Merge");
        MergeView mergeView = new MergeView(window, jsonTab);
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab.equals(jsonTab)) {
                mergeView.onTabSelected();
            }
        });
        return jsonTab;
    }


}
