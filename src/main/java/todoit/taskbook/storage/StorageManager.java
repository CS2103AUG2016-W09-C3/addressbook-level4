package todoit.taskbook.storage;

import com.google.common.eventbus.Subscribe;

import todoit.taskbook.commons.core.ComponentManager;
import todoit.taskbook.commons.core.LogsCenter;
import todoit.taskbook.commons.events.model.PresetChangedEvent;
import todoit.taskbook.commons.events.model.TaskBookChangedEvent;
import todoit.taskbook.commons.events.storage.DataSavingExceptionEvent;
import todoit.taskbook.commons.exceptions.DataConversionException;
import todoit.taskbook.model.ReadOnlyTaskBook;
import todoit.taskbook.model.UserPrefs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages storage of TaskBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskBookStorage taskBookStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(TaskBookStorage taskBookStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.taskBookStorage = taskBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(String taskBookFilePath, String userPrefsFilePath) {
        this(new XmlTaskBookStorage(taskBookFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ TaskBook methods ==============================

    @Override
    public void setTaskBookFilePath(String filePath) {
        taskBookStorage.setTaskBookFilePath(filePath);
    }
    
    @Override
    public String getTaskBookFilePath() {
        return taskBookStorage.getTaskBookFilePath();
    }

    @Override
    public Optional<ReadOnlyTaskBook> readTaskBook() throws DataConversionException, IOException {
        return readTaskBook(taskBookStorage.getTaskBookFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskBook> readTaskBook(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskBookStorage.readTaskBook(filePath);
    }

    @Override
    public void saveTaskBook(ReadOnlyTaskBook taskBook) throws IOException {
        saveTaskBook(taskBook, taskBookStorage.getTaskBookFilePath());
    }

    @Override
    public void saveTaskBook(ReadOnlyTaskBook taskBook, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        taskBookStorage.saveTaskBook(taskBook, filePath);
    }


    @Override
    @Subscribe
    public void handleTaskBookChangedEvent(TaskBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
    // @@author A0140155U
    @Override
    @Subscribe
    public void handlePresetChangedEvent(PresetChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local preset data changed, saving to file"));
        try {
            saveUserPrefs(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
    // @@author
}
