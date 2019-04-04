package settings;

import fileaccess.DataReader;
import fileaccess.DataWriter;
import lombok.Getter;
import mainPackage.Main;
import settings.models.Settings;

public class SettingsService {
    private static final String SETTINGS_SAVE_PATH = "";
    private static final String SETTINGS_SAVE_FILENAME = "_settings.data";

    private DataReader dataReader;
    private DataWriter dataWriter;
    @Getter
    private Settings settings;

    public SettingsService() {
        this.dataReader = new DataReader();
        this.dataWriter = new DataWriter();
    }

    public void loadAndApplySettings() {
        Object obj = dataReader.readObjectFromDisk(SETTINGS_SAVE_FILENAME, SETTINGS_SAVE_PATH);
        if (obj != null) {
            settings = (Settings) obj;
        } else {
            settings = new Settings();
        }
        applySettings();
    }

    public void saveCurrentSettings() {
        gatherApplicationProperties();
        dataWriter.writeObjectToDisk(settings, SETTINGS_SAVE_FILENAME, SETTINGS_SAVE_PATH);
    }

    private void applySettings() {
        Main.getWindow().setX(settings.getWindowX());
        Main.getWindow().setY(settings.getWindowY());
    }

    private void gatherApplicationProperties() {
        settings.setStageH(Main.getScene().getHeight());
        settings.setStageW(Main.getScene().getWidth());
        settings.setWindowX(Main.getWindow().getX());
        settings.setWindowY(Main.getWindow().getY());
    }
}
