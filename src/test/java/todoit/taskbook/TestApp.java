package todoit.taskbook;

import javafx.stage.Screen;
import javafx.stage.Stage;
import todoit.taskbook.MainApp;
import todoit.taskbook.commons.core.Config;
import todoit.taskbook.commons.core.GuiSettings;
import todoit.taskbook.model.ReadOnlyTaskBook;
import todoit.taskbook.model.UserPrefs;
import todoit.taskbook.storage.XmlSerializableTaskBook;
import todoit.taskbook.testutil.TestUtil;

import java.util.function.Supplier;

/**
 * This class is meant to override some properties of MainApp so that it will be suited for
 * testing
 */

public class TestApp extends MainApp {

    public static final String SAVE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("sampleData.xml");
    protected static final String DEFAULT_PREF_FILE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("pref_testing.json");
    protected static final String DEFAULT_CONFIG_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("config_testing.json");
    public static final String APP_TITLE = "Test App";
    protected static final String TASK_BOOK_NAME = "Test";
    protected Supplier<ReadOnlyTaskBook> initialDataSupplier = () -> null;
    protected String saveFileLocation = SAVE_LOCATION_FOR_TESTING;

    public TestApp() {
    }

    public TestApp(Supplier<ReadOnlyTaskBook> initialDataSupplier, String saveFileLocation) {
        super();
        this.initialDataSupplier = initialDataSupplier;
        this.saveFileLocation = saveFileLocation;

        // If some initial local data has been provided, write those to the file
        if (initialDataSupplier.get() != null) {
            TestUtil.createDataFileWithData(
                    new XmlSerializableTaskBook(this.initialDataSupplier.get()),
                    this.saveFileLocation);
        }
    }

    @Override
    protected Config initConfig(String configFilePath) {
        Config config = super.initConfig(DEFAULT_CONFIG_LOCATION_FOR_TESTING);
        config.setAppTitle(APP_TITLE);
        config.setTaskBookFilePath(saveFileLocation);
        config.setUserPrefsFilePath(DEFAULT_PREF_FILE_LOCATION_FOR_TESTING);
        config.setTaskBookName(TASK_BOOK_NAME);
        return config;
    }

    @Override
    protected UserPrefs initPrefs(Config config) {
        UserPrefs userPrefs = new UserPrefs();//super.initPrefs(config);
        userPrefs.clearPresets();
        double x = Screen.getPrimary().getVisualBounds().getMinX();
        double y = Screen.getPrimary().getVisualBounds().getMinY();
        userPrefs.updateLastUsedGuiSetting(new GuiSettings(600.0, 600.0, (int) x, (int) y));
        return userPrefs;
    }


    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }
    // @@author A0140155U
    public String getSaveFilePath(){
        return config.getTaskBookFilePath();
    }
    // @@author
    public static void main(String[] args) {
        launch(args);
    }
}