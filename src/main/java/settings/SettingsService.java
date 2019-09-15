package settings;

import csvmanagement.CsvImportService;
import csvmanagement.JsonMergeService;
import fileaccess.DataReader;
import fileaccess.DataWriter;
import lombok.Getter;
import mainPackage.Main;
import settings.models.Settings;

public class SettingsService {
    private static SettingsService SINGLE_INSTANCE = null;
    private static final String SETTINGS_SAVE_PATH = "";
    private static final String SETTINGS_SAVE_FILENAME = "_settings.data";

    private DataReader dataReader;
    private DataWriter dataWriter;
    @Getter
    private Settings settings;

    private SettingsService() {
        this.dataReader = new DataReader();
        this.dataWriter = new DataWriter();
    }

    public static SettingsService getInstance() {
        if (SINGLE_INSTANCE == null) {
            synchronized (JsonMergeService.class) {
                SINGLE_INSTANCE = new SettingsService();
            }
        }
        return SINGLE_INSTANCE;
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
        CsvImportService.getInstance().setInitialPath(settings.getCsvPath());
    }

    private void gatherApplicationProperties() {
        settings.setStageH(Main.getScene().getHeight());
        settings.setStageW(Main.getScene().getWidth());
        settings.setWindowX(Main.getWindow().getX());
        settings.setWindowY(Main.getWindow().getY());
        settings.setCsvPath(CsvImportService.getInstance().getInitialPath());
    }

    public boolean isSortJsonKeys() {
        return settings != null && settings.isSortJsonKeys();
    }

    public void setSortJsonKeys(boolean sort) {
        settings.setSortJsonKeys(sort);
    }
}
