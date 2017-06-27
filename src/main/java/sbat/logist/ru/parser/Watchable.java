package sbat.logist.ru.parser;

import java.nio.file.Path;

/**
 * Interface definition for a callback to be invoked when a file under
 * watch is changed.
 */
public interface Watchable {

    /**
     * Called when the file is created.
     * @param filePath The file path.
     */
    void onFileCreate(Path filePath);

    /**
     * Called when the file is modified.
     * @param filePath The file path.
     */
    void onFileModify(Path filePath);

    /**
     * Called when the file is deleted.
     * @param filePath The file path.
     */
    void onFileDelete(Path filePath);
}