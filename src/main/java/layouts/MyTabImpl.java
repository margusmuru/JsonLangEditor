package layouts;

import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

abstract class MyTabImpl implements MyTab {
    private Tab tab;
    VBox mainLayout;

    MyTabImpl() {
        this.tab = new Tab();
        this.mainLayout = new VBox();
    }

    public void createTabWithName(String input) {
        this.tab = new Tab(input);
    }

    @Override
    public Tab getLayout() {
        this.tab.setContent(mainLayout);
        return tab;
    }
}
