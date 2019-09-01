package layouts;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;

public class TabLayout implements MyLayout {
    private TabPane tabPane;

    public TabLayout() {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(UNAVAILABLE);
        tabPane.setTabMinWidth(200);
    }

    public void addTab(Tab tab) {
        this.tabPane.getTabs().add(tab);
    }

    public ReadOnlyObjectProperty<Tab> getSelectedItemProperty() {
        return tabPane.getSelectionModel().selectedItemProperty();
    }

    @Override
    public Control getLayout() {
        return tabPane;
    }
}
