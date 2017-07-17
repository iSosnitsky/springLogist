package sbat.logist.ru.parser.command;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.ZipInputStream;

@Component("toMessage")
public class FileToStringCommand implements Command<Path, String> {

    private static final String INCOMING_FILE_EXTENSION_ZIP = ".zip";
    private static final String INCOMING_FILE_EXTENSION_PKG = ".pkg";

    @Override
    public String execute(Path filePath) {
        Objects.requireNonNull(filePath);
        try {
            return getStringFromFile(filePath);
        } catch (IOException e) {
            throw new IllegalStateException("io exception occurred when parsing file to string", e);
        }
    }

    /**
     *
     * @param filePath
     * @return null if file was not imported
     * @throws IOException
     */
    private String getStringFromFile(Path filePath) throws IOException {
        final String result;
        if (filePath.toString().endsWith(INCOMING_FILE_EXTENSION_ZIP)) {
            result = readZipFileToUtf8String(filePath.toFile());
        } else if (filePath.toString().endsWith(INCOMING_FILE_EXTENSION_PKG)) {
            result = FileUtils.readFileToString(filePath.toFile(), StandardCharsets.UTF_8.displayName());
        }
        else
            throw new IllegalStateException(String.format("file {%s} must end with {%s} or {%s} ,file will not be imported", filePath.toString(), INCOMING_FILE_EXTENSION_ZIP, INCOMING_FILE_EXTENSION_PKG));
        return result;
    }

    /**
     * @return zip file with data as decompressed string
     */
    private String readZipFileToUtf8String(File file) throws IOException {
        byte[] buffer = new byte[2048];
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(file));
            // in our file should be only one entry
            zis.getNextEntry(); // get .pkg entry
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int len;
            while ((len = zis.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
            return output.toString("UTF-8");
        }
        finally {
            if (zis != null) zis.close();
        }
    }

}
